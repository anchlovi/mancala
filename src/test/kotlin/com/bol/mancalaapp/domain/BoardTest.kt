package com.bol.mancalaapp.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BoardTest {

    private val pits = listOf(1, 2, 3, 4, 5, 6, 0, 7, 8, 9, 10, 11, 12, 0)
    private val board = Board(pits, 6)

    @Test
    fun `should collect all stones into Mancalas`() {
        val result = board.collectAllStones()

        val expectedPits = listOf(0, 0, 0, 0, 0, 0, 21, 0, 0, 0, 0, 0, 0, 57)
        assertEquals(expectedPits, result.pits)
    }

    @Test
    fun `should correctly identify Mancala pit`() {
        assertTrue(board.isMancalaPit(6))
        assertFalse(board.isMancalaPit(5))
    }

    @Test
    fun `should correctly calculate Mancala pit for a row`() {
        assertEquals(6, board.getRowMancalaPit(0))
        assertEquals(13, board.getRowMancalaPit(1))
    }

    @Test
    fun `should correctly identify if pit is in a row`() {
        assertTrue(board.isPitInRow(0, 2))
        assertFalse(board.isPitInRow(0, 8))

        assertTrue(board.isPitInRow(1, 10))
        assertFalse(board.isPitInRow(1, 3))
    }

    @Test
    fun `should correctly calculate opposite pit index`() {
        assertEquals(12, board.getOppositePitIndex(0))
        assertEquals(2, board.getOppositePitIndex(10))
    }

    @Test
    fun `should correctly return pits in a row`() {
        assertEquals(listOf(1, 2, 3, 4, 5, 6), board.getPitsInRow(0))
        assertEquals(listOf(7, 8, 9, 10, 11, 12), board.getPitsInRow(1))
    }

    @Test
    fun `should correctly get stones in a pit`() {
        assertEquals(1, board.getStones(0))
        assertEquals(0, board.getStones(6))
    }

    @Test
    fun `should add a stone to each specified pit`() {
        val pitsToAddStone = listOf(1, 3, 5, 6, 12)

        val result = board.addStoneToPits(pitsToAddStone)

        val expectedPits = listOf(1, 3, 3, 5, 5, 7, 1, 7, 8, 9, 10, 11, 13, 0)
        assertEquals(expectedPits, result.pits)
    }

    @Test
    fun `should move all stones from one pit to another`() {
        val result = board.moveStones(
            fromPitIdx = 1,
            toPitIdx = 3
        )

        val expectedPits = listOf(1, 0, 3, 6, 5, 6, 0, 7, 8, 9, 10, 11, 12, 0)
        assertEquals(expectedPits, result.pits)
    }

    @Test
    fun `should empty the specified pit`() {
        val result = board.emptyPit(1)

        val expectedPits = listOf(1, 0, 3, 4, 5, 6, 0, 7, 8, 9, 10, 11, 12, 0)
        assertEquals(expectedPits, result.pits)
    }

    @Test
    fun `should correctly return total pits`() {
        assertEquals(pits.size, board.getTotalPits())
    }

    @Test
    fun `should create a new board with the specified configuration`() {
        // Arrange
        val totalRows = 2
        val pitsPerRow = 6
        val stonesPerPit = 4

        // Act
        val board = Board.createBoard(totalRows, pitsPerRow, stonesPerPit)

        // Assert
        val expectedPits = listOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0)
        assertEquals(expectedPits, board.pits)
    }
}