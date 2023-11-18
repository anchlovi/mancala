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
) {
    companion object {
        /**
         * Default number of players in a game of Mancala.
         */
        const val DEFAULT_NUMBER_OF_PLAYERS = 2

        /**
         * Default number of pits for each player in a game of Mancala, excluding the Mancalas.
         */
        const val DEFAULT_NUMBER_OF_PITS = 6

        /**
         * Default number of stones in each regular pit at the start of a game of Mancala.
         */
        const val DEFAULT_NUMBER_OF_STONES = 6

        /**
         * Default request for creating a new game of Mancala.
         * This request uses the default number of players, pits, and stones.
         */
        val DefaultCommand = CreateNewGameCommand(
            totalPlayers = DEFAULT_NUMBER_OF_PLAYERS,
            pitsForPlayer = DEFAULT_NUMBER_OF_PITS,
            stonesPerPit = DEFAULT_NUMBER_OF_STONES
        )
    }
}
