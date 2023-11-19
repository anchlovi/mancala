package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class DetermineWinnerGameRuleTest {
    private val rule = DetermineWinnerGameRule

    @Test
    fun `should be skipped if game is not over`() {
        val ctx = createEndGameCtx(player1WinnerPits)
            .withGameState(GameState.IN_PROGRESS)

        val newCtx = rule.apply(ctx)

        assertSame(ctx, newCtx)
    }

    @ParameterizedTest
    @MethodSource("pitsProvider")
    fun `should determine the correct winner`(pits: List<Int>, expectedWinner: Int, totalPlayers: Int) {
        val ctx = createEndGameCtx(pits, totalPlayers)

        val newCtx = rule.apply(ctx)

        assertEquals(expectedWinner, newCtx.game.winner)
    }

    @ParameterizedTest
    @MethodSource("drawPitsProvider")
    fun `should determine that a game ended with a draw`(pits: List<Int>, totalPlayers: Int) {
        val ctx = createEndGameCtx(pits, totalPlayers)

        val newCtx = rule.apply(ctx)

        assertNull(newCtx.game.winner)
    }

    companion object {
        val player1WinnerPits = listOf(0, 0, 8, 0, 0, 2)
        val player2WinnerPits = listOf(0, 0, 2, 0, 0, 8)
        val player3WinnerPits = listOf(0, 0, 2, 0, 0, 2, 0, 0, 8)

        val twoPlayersDrawPits = listOf(0, 0, 2, 0, 0, 2)
        val threePlayersDrawPits = listOf(0, 0, 2, 0, 0, 2, 0, 0, 2)

        fun createEndGameCtx(withPits: List<Int>, totalPlayers: Int = 2): GameContext {
            val ctx = GamesHelper.newGameContext()
                .withBoard(Board(withPits, pitsPerRow = 2))
                .withGameState(GameState.GAME_OVER)
            return ctx.copy(game = ctx.game.copy(totalPlayers = totalPlayers))
        }

        @JvmStatic
        fun pitsProvider(): List<Arguments> {
            return listOf(
                Arguments.of(player1WinnerPits, 0, 2),
                Arguments.of(player2WinnerPits, 1, 2),
                Arguments.of(player3WinnerPits, 2, 3)
            )
        }

        @JvmStatic
        fun drawPitsProvider(): List<Arguments> {
            return listOf(
                Arguments.of(twoPlayersDrawPits, 2),
                Arguments.of(threePlayersDrawPits, 3)
            )
        }
    }


}