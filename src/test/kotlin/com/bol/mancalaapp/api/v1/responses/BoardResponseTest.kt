package com.bol.mancalaapp.api.v1.responses

import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BoardResponseTest {

    @Test
    fun `should correctly convert from Board`() {
        val board = GamesHelper.newBoard()

        val response = BoardResponse.fromBoard(board)

        assertEquals(board.pits, response.pits)
        assertEquals(board.pitsForPlayer, response.pitsForPlayer)
    }
}