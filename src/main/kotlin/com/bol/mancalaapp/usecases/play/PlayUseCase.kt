package com.bol.mancalaapp.usecases.play

import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GamesRepository
import com.bol.mancalaapp.rules.GameContext
import com.bol.mancalaapp.rules.RulesEngine
import org.springframework.stereotype.Service
import java.util.concurrent.CompletionStage

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
     * @return A [CompletionStage] that, when completed, returns the updated game state.
     *         If the game is not found or if there is a version conflict, the CompletionStage completes exceptionally.
     */
    fun play(cmd: PlayCommand): CompletionStage<Game> {
        cmd.validate()

        return gamesRepository.findByIdAndVersion(cmd.gameId, cmd.version).thenCompose { game ->
            val updatedGameCtx = rulesEngine.apply(GameContext(game, cmd.pitIdx))
            gamesRepository.update(updatedGameCtx.game)
        }
    }
}