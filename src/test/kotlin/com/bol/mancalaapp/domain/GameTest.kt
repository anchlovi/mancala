package com.bol.mancalaapp.domain

import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GameTest {

    @Test
    fun `should correctly calculate the next player`() {
        val game = GamesHelper.newGame().copy(currentPlayer = 0, totalPlayers = 3)

        assertEquals(1, game.nextPlayer())
    }

    @Test
    fun `should wrap around to the first player after the last player`() {
        val game = GamesHelper.newGame().copy(currentPlayer = 2, totalPlayers = 3)

        assertEquals(0, game.nextPlayer())
    }
}