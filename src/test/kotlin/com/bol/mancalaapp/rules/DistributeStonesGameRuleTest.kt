package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.Player
import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

class DistributeStonesGameRuleTest {

    private val rule = DistributeStonesGameRule
    private val gameContext = GamesHelper.newGameContext().withPlayer(Player.PLAYER1)

    @Test
    fun `should distribute stones counterclockwise around the board skipping opponent Mancala when selected pit has stones`() {
        // set selected pit to have 6 stones to distribute around the board to test skipping opponent Mancala
        // and wrapping around the board
        val pit0Stones = 5
        val pit1Stones = 1
        val player1Mancala = 2
        val pit3Stones = 3
        val pit4Stones = 4
        val player2Mancala = 5

        val pits = listOf(pit0Stones, pit1Stones, player1Mancala, pit3Stones, pit4Stones, player2Mancala)
        val pitIdx = 0

        val ctx = gameContext.copy(pitIdx = pitIdx).withBoard(board = Board(pits, pitsPerRow = 2))

        val newCtx = rule.apply(ctx)

        pits.indices.forEach {
            val expectedStones = when(it) {
                pitIdx -> 1 // assert wrapped around board and distributed 1 stone to pit 0
                ctx.getPlayerMancalaIndex(Player.PLAYER2) -> player2Mancala // assert skipped opponent Mancala
                else -> pits[it] + 1 // assert distributed 1 stone to each pit
            }
            assertEquals(expectedStones, newCtx.board().getStones(it))
        }
    }

    @Test
    fun `should not distribute stones when selected pit has no stones`() {
        val pits = listOf(0, 1, 2, 3, 4, 5)
        val pitIdx = 0

        val ctx = gameContext.copy(pitIdx = pitIdx).withBoard(board = Board(pits, pitsPerRow = 2))

        val newCtx = rule.apply(ctx)

        assertSame(ctx, newCtx)
    }
}