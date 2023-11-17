package com.bol.mancalaapp.api.v1.requests

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.usecases.play.PlayCommand

/**
 * Data class representing the request to play a move in a Mancala game.
 * This class encapsulates the parameters required by the client to play a move.
 *
 * @property gameId The ID of the game in which the move is to be played.
 * @property pitIndex The index of the pit from which stones are to be moved.
 * @property version The version of the game state on which the move is based.
 */
data class PlayRequest(
    val gameId: GameId,
    val pitIndex: Int,
    val version: Int
) {
    /**
     * Converts this request into a [PlayCommand] to be used in the play use case.
     *
     * @return A [PlayCommand] reflecting the move specified in the request.
     */
    fun toCommand() = PlayCommand(gameId, pitIndex, version)
}