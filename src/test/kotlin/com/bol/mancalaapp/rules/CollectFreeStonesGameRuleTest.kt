package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CollectFreeStonesGameRuleTest {

    private val rule = CollectFreeStonesGameRule

    @Test
    fun `should be skipped if game is not over`() {
        val ctx = GamesHelper.newGameContext()
            .withGameState(GameState.IN_PROGRESS)

        val newCtx = rule.apply(ctx)

        assertSame(ctx, newCtx)
    }

    @ParameterizedTest
    @MethodSource("pitsProvider")
    fun `should empty all pits`(pits: List<Int>) {
        val ctx = GamesHelper.newGameContext()
            .withBoard(Board(pits, pitsPerRow = EndGameTestDataProvider.pitsForPlayer))
            .withGameState(GameState.GAME_OVER)

        val newCtx = rule.apply(ctx)

        assertTrue(newCtx.board().getPitsInRow(0).all { it == 0 })
        assertTrue(newCtx.board().getPitsInRow(1).all { it == 0 })
    }

    @ParameterizedTest
    @MethodSource("pitsProvider")
    fun `should move all the stones to Mancalas`(pits: List<Int>) {
        val ctx = GamesHelper.newGameContext()
            .withBoard(Board(pits, pitsPerRow = EndGameTestDataProvider.pitsForPlayer))
            .withGameState(GameState.GAME_OVER)

        val newCtx = rule.apply(ctx)

        val expectedPlayer1Score = ctx.board().getPitsInRow(0).sum() + EndGameTestDataProvider.player1MancalaStones
        val expectedPlayer2Score = ctx.board().getPitsInRow(1).sum() + EndGameTestDataProvider.player2MancalaStones

        assertEquals(expectedPlayer1Score, newCtx.board().getStones(newCtx.getPlayerMancalaIndex(0)))
        assertEquals(expectedPlayer2Score, newCtx.board().getStones(newCtx.getPlayerMancalaIndex(1)))
    }

    companion object {
        @JvmStatic
        fun pitsProvider(): List<List<Int>> = EndGameTestDataProvider.pitsProvider()
    }
}