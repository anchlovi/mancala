package com.bol.mancalaapp.rules

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Game rule for capturing stones in a Mancala game.
 *
 * This rule allows a player to capture stones if the last stone they placed lands in an empty pit on their side of the board.
 */
@Component
@Order(20)
object CaptureStonesGameRule : GameRule {
    override fun apply(ctx: GameContext): GameContext {
        if (captureRuleMet(ctx)) {
            val opponentPitIdx = ctx.getOppositePitIndex(ctx.lastPitIdx)

            val mancalaIdx = ctx.getPlayerMancalaIndex()
            val pits = ctx.pits().toMutableList()

            pits[mancalaIdx] += pits[opponentPitIdx] + 1
            pits[opponentPitIdx] = 0
            pits[ctx.lastPitIdx] = 0

            return ctx.withPits(pits)
        }

        return ctx
    }

    private fun captureRuleMet(ctx: GameContext): Boolean =
        ctx.getStonesInPit(ctx.lastPitIdx) == 1 &&
                ctx.getStonesInPit(ctx.getOppositePitIndex(ctx.lastPitIdx)) > 0 &&
                ctx.isPitOwnedByCurrentPlayer(ctx.lastPitIdx) &&
                ctx.getPlayerMancalaIndex() != ctx.lastPitIdx
}