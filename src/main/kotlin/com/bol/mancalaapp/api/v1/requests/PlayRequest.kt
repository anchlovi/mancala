package com.bol.mancalaapp.api.v1.requests

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.usecases.play.PlayCommand

/**
 * Data class representing the request to play a move in a Mancala game.
 * This class encapsulates the parameters required by the client to play a move.
 *
 * @property pitIndex The index of the pit from which stones are to be moved.
 * @property version The version of the game state on which the move is based.
 */
data class PlayRequest(
    val pitIndex: Int,
    val version: Int
) {
    /**
     * Converts this request into a [PlayCommand] to be used in the play use case.
     *
     * @param gameId The unique identifier of the game to be updated.
     * @return A [PlayCommand] reflecting the move specified in the request.
     */
    fun toCommand(gameId: GameId) = PlayCommand(gameId, pitIndex, version)
}