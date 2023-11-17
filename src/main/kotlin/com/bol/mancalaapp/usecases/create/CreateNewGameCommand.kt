package com.bol.mancalaapp.usecases.create

/**
 * Command object for creating a new Mancala game with specific game settings.
 *
 * This class encapsulates the parameters required to set up a new Mancala game, such as the number of pits for each player
 * and the initial number of stones in each pit. It also includes a method to calculate the total number of pits in the game.
 *
 * @property pitsForPlayer The number of players.
 * @property pitsForPlayer The number of pits for each player, excluding the Mancala.
 * @property stonesPerPit The initial number of stones in each regular pit at the start of the game.
 */
data class CreateNewGameCommand(
    val totalPlayers: Int,
    val pitsForPlayer: Int,
    val stonesPerPit: Int
)
