package com.bol.mancalaapp.domain

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BoardTest {

    @Test
    fun `should correctly identify Mancala pit`() {
        val pitsForPlayer = 6

        assertTrue(Board.isMancalaPit(6, pitsForPlayer))
        assertTrue(Board.isNotMancalaPit(5, pitsForPlayer))
    }

    @Test
    fun `should correctly identify pit ownership`() {
        val totalPits = 14

        assertTrue(Board.isPitOwnedByCurrentPlayer(Player.PLAYER1, 2, totalPits))
        assertFalse(Board.isPitOwnedByCurrentPlayer(Player.PLAYER1, 10, totalPits))

        assertTrue(Board.isPitOwnedByCurrentPlayer(Player.PLAYER2, 9, totalPits))
        assertFalse(Board.isPitOwnedByCurrentPlayer(Player.PLAYER2, 3, totalPits))
    }

    @Test
    fun `should correctly calculate opposite pit index`() {
        val totalPits = 14

        assertEquals(12, Board.getOppositePitIndex(totalPits, 0))
        assertEquals(2, Board.getOppositePitIndex(totalPits, 10))
    }

    @Test
    fun `should correctly return pits for player`() {
        val pits = listOf(1, 2, 3, 4, 5, 6, 0, 7, 8, 9, 10, 11, 12, 0)

        assertEquals(listOf(1, 2, 3, 4, 5, 6), Board.getPitsForPlayer(Player.PLAYER1, pits))
        assertEquals(listOf(7, 8, 9, 10, 11, 12), Board.getPitsForPlayer(Player.PLAYER2, pits))
    }
}