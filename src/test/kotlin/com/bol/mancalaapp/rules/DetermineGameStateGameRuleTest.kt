package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.domain.Player
import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class DetermineGameStateGameRuleTest {

    private val rule = DetermineGameStateGameRule

    @ParameterizedTest
    @MethodSource("pitsProvider")
    fun `should end game and set correct status when all pits for a player are empty`(pits: List<Int>) {
        val ctx = GamesHelper.newGameContext()
            .withPits(pits)
            .withGameState(GameState.IN_PROGRESS)

        val newCtx = rule.apply(ctx)

        assertEquals(GameState.GAME_OVER, newCtx.gameState())
    }

    @ParameterizedTest
    @MethodSource("pitsProvider")
    fun `should end game and empty all pits when all pits for a player are empty`(pits: List<Int>) {
        val ctx = GamesHelper.newGameContext()
            .withPits(pits)
            .withGameState(GameState.IN_PROGRESS)

        val newCtx = rule.apply(ctx)

        assertTrue(Board.getPitsForPlayer(Player.PLAYER1, newCtx.pits()).all { it == 0 })
        assertTrue(Board.getPitsForPlayer(Player.PLAYER2, newCtx.pits()).all { it == 0 })
    }

    @ParameterizedTest
    @MethodSource("pitsProvider")
    fun `should end game and move all pits to Mancalas when all pits for a player are empty`(pits: List<Int>) {
        val ctx = GamesHelper.newGameContext()
            .withPits(pits)
            .withGameState(GameState.IN_PROGRESS)

        val newCtx = rule.apply(ctx)

        val expectedPlayer1Score = Board.getPitsForPlayer(Player.PLAYER1, pits).sum() + player1MancalaStones
        val expectedPlayer2Score = Board.getPitsForPlayer(Player.PLAYER2, pits).sum() + player2MancalaStones

        assertEquals(expectedPlayer1Score, newCtx.getStonesInPit(newCtx.getPlayerMancalaIndex(Player.PLAYER1)))
        assertEquals(expectedPlayer2Score, newCtx.getStonesInPit(newCtx.getPlayerMancalaIndex(Player.PLAYER2)))
    }

    @Test
    fun `should not end game when not all pits for a player are empty`() {
        val ctx = GamesHelper.newGameContext()
            .withPits(gameInProgressPits)
            .withGameState(GameState.IN_PROGRESS)

        val newCtx = rule.apply(ctx)

        assertSame(ctx, newCtx)
    }

    companion object {
        val pitsForPlayer = (4..6).random()

        val player1MancalaStones = (1 until 30).random()
        val player2MancalaStones = (1 until 30).random()

        val pitsWithStones = List(pitsForPlayer) { (1 until 12).random() }
        val pitsWithoutStones = List(pitsForPlayer) { 0 }

        val player1WithoutStonesPits = pitsWithoutStones + player1MancalaStones + pitsWithStones + player2MancalaStones
        val player2WithoutStonesPits = pitsWithStones + player1MancalaStones + pitsWithoutStones + player2MancalaStones

        val gameInProgressPits = pitsWithStones + player1MancalaStones + pitsWithStones + player2MancalaStones

        @JvmStatic
        fun pitsProvider(): List<List<Int>> {
            return listOf(player1WithoutStonesPits, player2WithoutStonesPits)
        }
    }
}