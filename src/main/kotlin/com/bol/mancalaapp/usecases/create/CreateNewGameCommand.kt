package com.bol.mancalaapp.usecases.create

private const val DEFAULT_INITIAL_NUMBER_OF_PLAYERS = 2

/**
 * Command object for creating a new Mancala game with specific game settings.
 *
 * This class encapsulates the parameters required to set up a new Mancala game, such as the number of pits for each player
 * and the initial number of stones in each pit. It also includes a method to calculate the total number of pits in the game.
 *
 * @property numberOfPitsForPlayer The number of pits for each player, excluding the Mancala (store).
 * @property numberOfStonesInEachPit The initial number of stones in each regular pit at the start of the game.
 */
data class CreateNewGameCommand(
    val numberOfPitsForPlayer: Int,
    val numberOfStonesInEachPit: Int
) {
    /**
     * Calculates the total number of pits on the game board, including the Mancalas.
     *
     * This calculation is based on the number of pits for each player and assumes a default number of players.
     *
     * @return The total number of pits on the board.
     */
    fun totalNumberOfPits(): Int =
        numberOfPitsForPlayer * DEFAULT_INITIAL_NUMBER_OF_PLAYERS + DEFAULT_INITIAL_NUMBER_OF_PLAYERS
}
