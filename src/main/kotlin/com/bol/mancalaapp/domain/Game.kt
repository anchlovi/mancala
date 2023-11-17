package com.bol.mancalaapp.domain

import com.bol.mancalaapp.GameId

/**
 * Represents the current state of the Mancala game.
 */
enum class GameState {
    IN_PROGRESS,
    GAME_OVER;

    /**
     * Checks if the game is over.
     *
     * @return True if the game state is GAME_OVER, false otherwise.
     */
    val isGameOver: Boolean
        get() = this == GAME_OVER
}

/**
 * Represents a game of Mancala, encapsulating all relevant game details.
 *
 * @property id Unique identifier for the game.
 * @property board The current state of the game board.
 * @property totalPlayers The total number of players in the game.
 * @property currentPlayer The player who is currently making a move.
 * @property gameState The current state of the game (in progress or game over).
 * @property winner The player who has won the game, if applicable. Null if the game is still in progress or if it's a draw.
 * @property version The version number of the game state, useful for managing state changes and concurrency.
 */
data class Game(
    val id: GameId,
    val board: Board,
    val totalPlayers: Int,
    val currentPlayer: Int,
    val gameState: GameState,
    val winner: Int?,
    val version: Int
) {
    /**
     * Calculates the next player's turn in the game.
     *
     * @return The number of the next player.
     */
    fun nextPlayer() = (currentPlayer + 1) % totalPlayers
}
