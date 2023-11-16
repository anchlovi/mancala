package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.Player
import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CaptureStonesGameRuleTest {

    private val rule = CaptureStonesGameRule

    private val stonesToCapture = 4
    private val initialStonesInMancala = 7
    private val notEmptyPitStones = 2

    private lateinit var ctx: GameContext

    @BeforeEach
    fun setUp() {
        val pits = listOf(
            notEmptyPitStones, // idx 0
            1, // idx 1
            1, // idx 2
            2, // idx 3
            initialStonesInMancala, // Player 1 Mancala
            1, // idx 5
            0, // idx 6
            stonesToCapture, // idx 7
            stonesToCapture, // idx 8
            0 // Player 2 Mancala
        )

        ctx = GamesHelper.newGameContext()
            .withPlayer(Player.PLAYER1)
            .withPits(pits)
    }

    @Test
    fun `should capture stones when last stone lands in an empty pit owned by the current player and the opposite pit has stones`() {
        val lastIdx = 1
        val oppositeIdx = Board.getOppositePitIndex(ctx.totalPits(), lastIdx)

        val newCtx = rule.apply(ctx.withLastPitIdx(lastIdx))

        val expectedStonesInMancala = initialStonesInMancala + stonesToCapture + 1 // 1 stone from last pit

        assertEquals(expectedStonesInMancala, newCtx.getStonesInPit(newCtx.getPlayerMancalaIndex()))
        assertEquals(0, newCtx.getStonesInPit(lastIdx))
        assertEquals(0, newCtx.getStonesInPit(oppositeIdx))
    }

    @Test
    fun `should not capture stones when last stone does not land in an empty pit owned by the current player`() {
        val lastIdx = 0

        val newCtx = rule.apply(ctx.withLastPitIdx(lastIdx))

        assertEquals(ctx.pits(), newCtx.pits())
    }

    @Test
    fun `should capture stones when last stone lands in an empty pit owned by the current player and the opposite pit has no stones`() {
        val lastIdx = 2

        val newCtx = rule.apply(ctx.withLastPitIdx(lastIdx))

        assertEquals(ctx.pits(), newCtx.pits())
    }

    @Test
    fun `should not capture stones when last stone land in Mancala`() {
        val lastIdx = Board.getPlayerMancalaIndex(ctx.player(), 4)

        val newCtx = rule.apply(ctx.withLastPitIdx(lastIdx))

        assertEquals(ctx.pits(), newCtx.pits())
    }

    @Test
    fun `should not capture stones when last stone land in an empty pit not owned by the current player`() {
        val lastIdx = 5

        val newCtx = rule.apply(ctx.withLastPitIdx(lastIdx))

        assertEquals(ctx.pits(), newCtx.pits())
    }
}