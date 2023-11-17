package com.bol.mancalaapp.api.v1.requests

import com.bol.mancalaapp.usecases.create.CreateNewGameCommand

/**
 * Data class representing the request to create a new Mancala game.
 * This class encapsulates the parameters required by the client to initiate a new game.
 * It includes validation annotations to ensure that the provided data meets specific criteria.
 *
 * @property totalPlayers The total number of players in the game.
 * @property pitsForPlayer The number of pits for each player, excluding the Mancalas.
 *                         Represents the player's choice for the complexity of the game.
 * @property stonesPerPit The initial number of stones in each regular pit at the start of the game.
 *                        Determines the initial setup of the game.
 */
data class CreateNewGameRequest(
    val totalPlayers: Int,
    val pitsForPlayer: Int,
    val stonesPerPit: Int
) {
    /**
     * Converts this request into a [CreateNewGameCommand] to be used in the game creation use case.
     *
     * @return A [CreateNewGameCommand] reflecting the settings provided in the request.
     */
    fun toCommand() = CreateNewGameCommand(totalPlayers, pitsForPlayer, stonesPerPit)
}

