package com.bol.mancalaapp.domain

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * Represents the game board in Mancala, encapsulating the state of all pits including Mancalas.
 *
 * @property pits A list representing the number of stones in each pit.
 * @property pitsPerRow The total number of regular pits for each player.
 */
data class Board(
    val pits: List<Int>,
    val pitsPerRow: Int
) {
    /**
     * Determines if a given pit is a Mancala pit.
     *
     * @param pitIdx The index of the pit to check.
     * @return True if the pit is a Mancala pit, false otherwise.
     */
    fun isMancalaPit(pitIdx: Int) = isMancalaPit(pitIdx, pitsPerRow)

    /**
     * Calculates the index of the Mancala in a given row.
     *
     * @param row The row whose Mancala pit index is to be calculated.
     * @return The index of the row's Mancala pit.
     */
    fun getRowMancalaPit(row: Int) = pitsPerRow * (row + 1) + row

    /**
     * Determines if a given pit is in a given row.
     *
     * @param row The row to check.
     * @param pitIdx The index of the pit to check.
     * @return True if the pit is in the given row, false otherwise.
     */
    fun isPitInRow(row: Int, pitIdx: Int): Boolean = pitIdx in rowStart(row) until rowEnd(row)

    /**
     * Calculates the starting index of a given row on the board.
     *
     * @param row The row number for which to calculate the starting index.
     * @return The starting index of the given row on the board.
     */
    private fun rowStart(row: Int) = pitsPerRow * row + row

    /**
     * Calculates the ending index of a given row on the board.
     *
     * @param row The row number for which to calculate the ending index.
     * @return The ending index of the given row on the board.
     */
    private fun rowEnd(row: Int) = pitsPerRow * (row + 1) + row

    /**
     * Calculates the index of the pit opposite to a given pit index.
     *
     * @param pitIdx The index of the pit for which to find the opposite.
     * @return The index of the opposite pit.
     */
    fun getOppositePitIndex(pitIdx: Int): Int {
        val currentRow = pitIdx / pitsPerRow
        val totalRows = pits.size / (pitsPerRow + 1)
        val oppositeRow = (currentRow + 1) % totalRows

        val currentStartIdx = rowStart(currentRow)
        val oppositeEndIdx = rowEnd(oppositeRow) - 1 // -1 to exclude Mancala

        val positionInRow = pitIdx - currentStartIdx

        return oppositeEndIdx - positionInRow
    }

    /**
     * Returns the pits in a given player (without Mancala).
     *
     * @param row The row whose pits are to be returned.
     * @return A list of integers representing the number of stones in each pit for the given row.
     */
    fun getPitsInRow(row: Int): List<Int> = pits.subList(rowStart(row), rowEnd(row))

    /**
     * Gets the number of stones in a specific pit.
     *
     * @param pitIdx The index of the pit.
     */
    fun getStones(pitIdx: Int) = pits[pitIdx]

    /**
     * Returns the total number of pits on the board, including Mancalas.
     *
     * @return The total number of pits on the board.
     */
    @JsonIgnore
    fun getTotalPits() = pits.size

    /**
     * Adds a stone to each pit in the provided list and returns a new Board instance.
     *
     * @param pits A list of pit indices where a stone should be added.
     * @return A new [Board] instance with the updated number of stones in the specified pits.
     */
    fun addStoneToPits(pits: List<Int>): Board = apply {
        pits.forEach { this[it] += 1 }
    }

    /**
     * Moves all stones from the pit at the given index to another pit and returns a new Board instance.
     *
     * @param fromPitIdx The index of the pit from which to move the stones.
     * @param toPitIdx The index of the pit to which to move the stones.
     * @return A new [Board] instance with the stones moved from the pit at `fromPitIdx` to the pit at `toPitIdx`.
     */
    fun moveStones(fromPitIdx: Int, toPitIdx: Int): Board = apply {
        this[toPitIdx] += this[fromPitIdx]
        this[fromPitIdx] = 0
    }

    /**
     * Empties the pit at the given index and returns a new Board instance.
     *
     * @param pitIdx The index of the pit to be emptied.
     * @return A new [Board] instance with the pit at the given index emptied.
     */
    fun emptyPit(pitIdx: Int): Board = apply {
        this[pitIdx] = 0
    }

    /**
     * Collects all stones from each row's pits into their respective Mancala, empties all non-Mancala pits
     * and returns a new Board instance.
     *
     * @return A new [Board] instance with all stones collected into their respective Mancalas and all non-Mancala pits emptied.
     */
    fun collectAllStones(): Board {
        val totalRows = pits.size / (pitsPerRow + 1)

        return apply {
            (0 until totalRows).forEach { row ->
                val mancalaIdx = getRowMancalaPit(row)
                val stonesInRow = getPitsInRow(row).sum()

                this[mancalaIdx] += stonesInRow
            }

            pits.indices.forEach {
                if (isMancalaPit(it).not()) this[it] = 0
            }
        }
    }

    /**
     * Applies a given block of code to a mutable list of pits and returns a new Board instance with updated pits.
     *
     * @param block A block of code to be applied to a mutable list of pits.
     * @return A new [Board] instance with the pits updated according to the provided block of code.
     */
    private fun apply(block: MutableList<Int>.() -> Unit): Board {
        val updatedPits = pits.toMutableList().apply(block)
        return copy(pits = updatedPits)
    }

    companion object {
        /**
         * Creates a new Board instance with the specified number of rows, pits per row, and initial number of stones per pit.
         *
         * @param totalRows The total number of rows on the board.
         * @param pitsPerRow The number of pits in each row, excluding the Mancala.
         * @param stonesPerPit The initial number of stones in each regular pit.
         * @return A new [Board] instance with the specified configuration.
         */
        fun createBoard(totalRows: Int, pitsPerRow: Int, stonesPerPit: Int): Board {
            val totalPits = totalRows * pitsPerRow + totalRows

            val pits = List(totalPits) { index ->
                when {
                    isMancalaPit(index, pitsPerRow) -> 0
                    else -> stonesPerPit
                }
            }

            return Board(pits, pitsPerRow)
        }

        private fun isMancalaPit(pitIdx: Int, pitsPerRow: Int) = pitIdx % (pitsPerRow + 1) == pitsPerRow
    }
}