package com.bol.mancalaapp.api.v1.responses

import com.bol.mancalaapp.domain.Game
import java.util.*

/**
 * Data class representing the response structure for the state of a Mancala game.
 * This class encapsulates the relevant details of a game's state, making it suitable for API responses.
 *
 * @property id The unique identifier of the game.
 * @property board The current state of the game board represented as a [BoardResponse].
 * @property currentPlayer The name of the player who has the current turn.
 * @property gameState The current state of the game (e.g., IN_PROGRESS, GAME_OVER) as a String.
 * @property winner The player who won the game, null if the game is still in progress or if it's a draw.
 * @property version The version of the game state, used for optimistic locking.
 */
data class GameResponse(
    val id: UUID,
    val board: BoardResponse,
    val totalPlayers: Int,
    val currentPlayer: Int,
    val gameState: String,
    val winner: Int? = null,
    val version: Int
) {
    companion object {
        /**
         * Factory method to create a [GameResponse] from a [Game] domain object.
         *
         * This method transforms the detailed game domain object into a simplified response format,
         * including the current state of the board, the current player, the game state, the winner (if any), and the game version.
         *
         * @param game The [Game] domain object to be transformed.
         * @return A [GameResponse] representing the state of the provided [Game].
         */
        fun fromGame(game: Game): GameResponse {
            return GameResponse(
                id = game.id,
                board = BoardResponse.fromBoard(game.board),
                totalPlayers = game.totalPlayers,
                currentPlayer = game.currentPlayer,
                gameState = game.gameState.name,
                winner = game.winner,
                version = game.version
            )
        }
    }
}
