package com.bol.mancalaapp.api.v1.responses

import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GameResponseTest {

    @Test
    fun `should correctly convert from Game`() {
        val game = GamesHelper.newGame()

        val response = GameResponse.fromGame(game)

        assertEquals(game.id, response.id)
        assertEquals(BoardResponse.fromBoard(game.board), response.board)
        assertEquals(game.currentPlayer.name, response.currentPlayer)
        assertEquals(game.gameState.name, response.gameState)
        assertEquals(game.winner?.name, response.winner)
        assertEquals(game.version, response.version)
    }
}