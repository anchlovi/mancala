package com.bol.mancalaapp.domain

/**
 * Represents the game board in Mancala, encapsulating the state of all pits including Mancalas.
 *
 * @property pits A list representing the number of stones in each pit.
 * @property pitsForPlayer The total number of regular pits for each player.
 */
data class Board(
    val pits: List<Int>,
) {

    val pitsForPlayer = (pits.size - 1) / 2

    companion object {
        /**
         * Calculates the index of the Mancala pit for a given player.
         *
         * @param player The player whose Mancala pit index is to be calculated.
         * @param pitsForPlayer The total number of regular pits for each player.
         * @return The index of the player's Mancala pit.
         */
        fun getPlayerMancalaIndex(player: Player, pitsForPlayer: Int) =
            pitsForPlayer + player.ordinal * pitsForPlayer + player.ordinal

        /**
         * Determines if a given pit is a Mancala pit.
         *
         * @param pitIdx The index of the pit to check.
         * @param pitsForPlayer The total number of regular pits for each player.
         * @return True if the pit is a Mancala pit, false otherwise.
         */
        fun isMancalaPit(pitIdx: Int, pitsForPlayer: Int) = (pitIdx + 1) % (pitsForPlayer + 1) == 0

        /**
         * Determines if a given pit is not a Mancala pit.
         *
         * @param pitIdx The index of the pit to check.
         * @param pitsForPlayer The total number of regular pits for each player.
         * @return True if the pit is not a Mancala pit, false otherwise.
         */
        fun isNotMancalaPit(pitIdx: Int, pitsForPlayer: Int): Boolean  = isMancalaPit(pitIdx, pitsForPlayer).not()

        /**
         * Determines if a given pit is owned by the current player.
         *
         * @param player The current player.
         * @param pitIdx The index of the pit to check.
         * @param totalPits The total number of pits on the board, including Mancalas.
         * @return True if the pit is owned by the current player, false otherwise.
         */
        fun isPitOwnedByCurrentPlayer(player: Player, pitIdx: Int, totalPits: Int): Boolean {
            val pitsPerSide = (totalPits / 2) - 1 // Subtract 1 for the Mancala

            return when (player) {
                Player.PLAYER1 -> pitIdx in 0 until pitsPerSide
                Player.PLAYER2 -> pitIdx in pitsPerSide + 1 until (pitsPerSide * 2) + 1 // Add 1 for the Mancala
            }
        }

        /**
         * Calculates the index of the pit opposite to a given pit index.
         *
         * @param totalPits The total number of pits on the board, including Mancalas.
         * @param pitIdx The index of the pit for which to find the opposite.
         * @return The index of the opposite pit.
         */
        fun getOppositePitIndex(totalPits: Int, pitIdx: Int): Int = totalPits - pitIdx - 2

        /**
         * Returns the pits for a given player.
         *
         * @param player The player whose pits are to be returned.
         * @param pits The list of all pits on the board, including Mancalas.
         * @return A list of integers representing the number of stones in each pit for the given player.
         */
        fun getPitsForPlayer(player: Player, pits: List<Int>): List<Int> {
            val half = (pits.size / 2) - 1 // Subtract 1 for the Mancala
            return when (player) {
                Player.PLAYER1 -> pits.subList(0, half)
                Player.PLAYER2 -> pits.subList(half + 1, half * 2 + 1) // Skip the first player's Mancala
            }
        }
    }
}