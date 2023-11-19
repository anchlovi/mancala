package com.bol.mancalaapp.usecases.play

import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GamesRepository
import com.bol.mancalaapp.rules.GameContext
import com.bol.mancalaapp.rules.RulesEngine
import org.springframework.stereotype.Service

/**
 * Service class responsible for handling a play action in a Mancala game.
 * This class manages the process of executing a game move based on a given command, applying the game rules, and updating the game state.
 *
 * @property gamesRepository The repository responsible for managing game data retrieval and persistence.
 * @property rulesEngine The engine responsible for applying game rules and determining the outcome of a play action.
 */
@Service
class PlayUseCase(
    private val gamesRepository: GamesRepository,
    private val rulesEngine: RulesEngine
) {
    /**
     * Executes a play action in a game of Mancala.
     *
     * The method retrieves the current game state, applies the game rules based on the play command,
     * and updates the game with the new state. It employs optimistic locking using the game version for concurrency control.
     *
     * @param cmd The command containing the details of the play action (game ID, pit index, and game version).
     * @return The updated [Game] state.
     *
     * @throws com.bol.mancalaapp.domain.GameNotFoundException If no game is found with the provided identifier and version.
     * @throws com.bol.mancalaapp.domain.VersionMismatchException If there is a version conflict during the update.
     * @throws com.bol.mancalaapp.rules.validators.ValidationException If the player move fails validation.
     */
    fun play(cmd: PlayCommand): Game {
        cmd.validate()

        val game = gamesRepository.findByIdAndVersion(cmd.gameId, cmd.version)
        val ctx = rulesEngine.apply(GameContext(game, cmd.pitIdx))

        return gamesRepository.update(ctx.game)
    }
}