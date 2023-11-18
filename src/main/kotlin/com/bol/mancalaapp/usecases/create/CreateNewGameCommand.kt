package com.bol.mancalaapp.usecases.create

/**
 * Command object for creating a new Mancala game with specific game settings.
 *
 * This class encapsulates the parameters required to set up a new Mancala game, such as the total number of players,
 * the number of pits for each player (excluding Mancalas), and the initial number of stones in each regular pit.
 * It includes validation to ensure the game settings are within acceptable limits.
 *
 * @property totalPlayers The total number of players in the game.
 * @property pitsForPlayer The number of pits for each player, excluding the Mancala.
 * @property stonesPerPit The initial number of stones in each regular pit at the start of the game.
 */
data class CreateNewGameCommand(
    val totalPlayers: Int,
    val pitsForPlayer: Int,
    val stonesPerPit: Int
) {
    /**
     * Validates the game settings to ensure they are within acceptable limits.
     *
     * @throws IllegalArgumentException If the game settings are not within acceptable limits.
     */
    fun validate() {
        require(totalPlayers > 1) { "Total players must be greater than 1" }
        require(totalPlayers < 6) { "Total players must be less than 6" }
        require(pitsForPlayer > 1) { "Pits per player must be greater than 1" }
        require(pitsForPlayer < 15) { "Pits per player must be less than 15" }
        require(stonesPerPit > 1) { "Stones per pit must be greater than 1" }
        require(stonesPerPit < 20) { "Stones per pit must be less than 20" }
    }

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
         * Default command for creating a new game of Mancala.
         * This command uses the default number of players, pits, and stones.
         */
        val DefaultCommand = CreateNewGameCommand(
            totalPlayers = DEFAULT_NUMBER_OF_PLAYERS,
            pitsForPlayer = DEFAULT_NUMBER_OF_PITS,
            stonesPerPit = DEFAULT_NUMBER_OF_STONES
        )
    }
}
