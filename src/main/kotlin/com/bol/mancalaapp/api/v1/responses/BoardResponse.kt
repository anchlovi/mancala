package com.bol.mancalaapp.api.v1.responses

import com.bol.mancalaapp.domain.Board

/**
 * Data class representing the response structure for the state of a Mancala game board.
 * This class provides a simplified view of the game board, suitable for API responses.
 *
 * @property pits A list representing the number of stones in each pit on the board, including Mancalas.
 * @property pitsForPlayer The number of regular pits (excluding Mancalas) for each player.
 */
data class BoardResponse(
    val pits: List<Int>,
    val pitsForPlayer: Int
) {
    companion object {
        /**
         * Factory method to create a [BoardResponse] from a [Board] domain object.
         *
         * This method is used to convert the internal representation of a game board into a format suitable for API responses.
         *
         * @param board The [Board] domain object to be transformed.
         * @return A [BoardResponse] representing the state of the provided [Board].
         */
        fun fromBoard(board: Board): BoardResponse {
            return BoardResponse(
                pits = board.pits,
                pitsForPlayer = board.pitsForPlayer
            )
        }
    }
}
