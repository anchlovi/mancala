package com.bol.mancalaapp.usecases.play

import com.bol.mancalaapp.GameId

/**
 * Command object representing a player's action to play a move in a Mancala game.
 *
 * This class encapsulates the necessary details for performing a move, such as the game's unique identifier,
 * the index of the pit from which stones are to be played, and the version of the game for concurrency control.
 *
 * @property gameId The unique identifier of the game in which the move is to be played.
 * @property pitIdx The index of the pit from which the stones will be played.
 *                  It represents the player's choice of pit to play their turn.
 * @property version The version of the game at the time when the move is initiated.
 *                   Used for implementing optimistic locking to handle concurrent game updates.
 */
data class PlayCommand(
    val gameId: GameId,
    val pitIdx: Int,
    val version: Int
)

