package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.helpers.GamesHelper
import com.bol.mancalaapp.rules.EndGameTestDataProvider.gameInProgressPits
import com.bol.mancalaapp.rules.EndGameTestDataProvider.pitsForPlayer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class GameOverGameRuleTest {

    private val rule = GameOverGameRule

    @ParameterizedTest
    @MethodSource("pitsProvider")
    fun `should end game and set correct status when all pits for a player are empty`(pits: List<Int>) {
        val ctx = GamesHelper.newGameContext()
            .withBoard(board = Board(pits, pitsPerRow = pitsForPlayer))
            .withGameState(GameState.IN_PROGRESS)

        val newCtx = rule.apply(ctx)

        assertEquals(GameState.GAME_OVER, newCtx.gameState())
    }

    @Test
    fun `should not end game when not all pits for a player are empty`() {
        val ctx = GamesHelper.newGameContext()
            .withBoard(Board(gameInProgressPits, pitsPerRow = pitsForPlayer))
            .withGameState(GameState.IN_PROGRESS)

        val newCtx = rule.apply(ctx)

        assertSame(ctx, newCtx)
    }

    @Test
    fun `should end game when all pits for all players are empty`() {
        val pits = listOf(0, 0, 2, 0, 0, 6)
        val ctx = GamesHelper.newGameContext()
            .withBoard(Board(pits, pitsPerRow = 2))
            .withGameState(GameState.IN_PROGRESS)

        val newCtx = rule.apply(ctx)

        assertEquals(GameState.GAME_OVER, newCtx.gameState())
    }

    companion object {
        @JvmStatic
        fun pitsProvider(): List<List<Int>> = EndGameTestDataProvider.pitsProvider()
    }
}