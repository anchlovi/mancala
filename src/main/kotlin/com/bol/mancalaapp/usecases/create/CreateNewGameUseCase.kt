package com.bol.mancalaapp.usecases.create

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.*
import org.springframework.stereotype.Service
import java.util.concurrent.CompletionStage

/**
 * Service class responsible for handling the creation of a new Mancala game.
 * This class contains the business logic to instantiate and persist a new game based on the provided command.
 *
 * @property gamesRepository The repository responsible for managing game data persistence.
 */
@Service
class CreateNewGameUseCase(
    private val gamesRepository: GamesRepository
) {
    /**
     * Creates a new game based on the specified command and persists it in the repository.
     *
     * @param command The command containing the settings for the new game.
     * @return A [CompletionStage] that, when completed, returns the newly created game.
     */
    fun createNewGame(command: CreateNewGameCommand): CompletionStage<Game> {
        val game = createGame(command)
        return gamesRepository.create(game).thenApply { game }
    }

    /**
     * Instantiates a new [Game] object based on the provided command.
     * Sets up the initial game state, including the board, current player, game state, and version.
     *
     * @param command The command containing the settings for the new game.
     * @return A new [Game] object representing the initial state of the game.
     */
    private fun createGame(command: CreateNewGameCommand): Game = Game(
        id = GameId.randomUUID(),
        board = createBoard(command),
        currentPlayer = Player.PLAYER1,
        gameState = GameState.IN_PROGRESS,
        winner = null,
        version = 0
    )

    /**
     * Creates a [Board] object based on the settings specified in the command.
     * Initializes the pits and sets up the initial distribution of stones.
     *
     * @param command The command containing the settings for the new game's board.
     * @return A [Board] object representing the initial state of the game board.
     */
    private fun createBoard(command: CreateNewGameCommand): Board {
        val pitsList = List(command.totalNumberOfPits()) { index ->
            when {
                Board.isMancalaPit(index, command.numberOfPitsForPlayer) -> 0
                else -> command.numberOfStonesInEachPit
            }
        }

        return Board(
            pits = pitsList
        )
    }
}