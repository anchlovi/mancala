package com.bol.mancalaapp.rules

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Game rule for distributing stones around the Mancala board.
 *
 * This rule takes care of moving the stones from the selected pit and distributing them
 * counterclockwise around the board, skipping the opponent's Mancala pit.
 *
 * Stones are moved one by one into subsequent pits in a counterclockwise direction.
 * The opponent's Mancala pit is skipped during this distribution. The distribution
 * continues until all stones from the selected pit have been placed.
 */
@Component
@Order(10)
object DistributeStonesGameRule : GameRule {
    override fun apply(ctx: GameContext): GameContext {
        if (ctx.getStonesInPit(ctx.pitIdx) == 0) {
            return ctx
        }

        val pits = ctx.pits().toMutableList()
        val opponentMancalaIdx = ctx.getPlayerMancalaIndex(ctx.player().opponent)
        var currentPitIdx = ctx.pitIdx

        var stonesToDistribute = pits[ctx.pitIdx]
        pits[ctx.pitIdx] = 0

        while (stonesToDistribute > 0) {
            currentPitIdx = (currentPitIdx + 1) % pits.size

            if (currentPitIdx == opponentMancalaIdx) {
                continue
            }

            pits[currentPitIdx] += 1
            stonesToDistribute -= 1
        }

        return ctx.withPits(pits)
            .withLastPitIdx(currentPitIdx)
    }
}
