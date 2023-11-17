package com.bol.mancalaapp.rules

import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DetermineNextPlayerGameRuleTest {

    private val rule = DetermineNextPlayerGameRule
    private val gameContext = GamesHelper.newGameContext()

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
}