package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DetermineNextPlayerGameRuleTest {

    private val rule = DetermineNextPlayerGameRule
    private val gameContext = GamesHelper.newGameContext().withGameState(GameState.IN_PROGRESS)

    @Test
    fun `should not change player when last stone lands in player's own Mancala`() {
        val ctx = gameContext.withLastPitIdx(gameContext.getPlayerMancalaIndex())

        val newCtx = rule.apply(ctx)

        assertEquals(gameContext.player(), newCtx.player())
    }

    @Test
    fun `should change player when last stone does not land in player's own Mancala`() {
        val ctx = gameContext.withLastPitIdx(gameContext.getPlayerMancalaIndex() - 1)

        val newCtx = rule.apply(ctx)

        assertEquals(gameContext.game.nextPlayer(), newCtx.player())
    }

    @Test
    fun `should change player to the next player that can play`() {
        val pits = listOf(
            1, 2, 0, // player 0
            0, 0, 0, // player 1
            1, 0, 0  // player 2
        )
        val ctx = gameContext
            .copy(game = gameContext.game.copy(
                totalPlayers = 3,
                board = gameContext.game.board.copy(pits = pits, pitsPerRow = 2)
            ))
            .withLastPitIdx(1)

        val newCtx = rule.apply(ctx)

        assertEquals(2, newCtx.player())
    }
}