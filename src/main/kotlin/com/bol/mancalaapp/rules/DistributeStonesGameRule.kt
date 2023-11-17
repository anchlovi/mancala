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
        if (ctx.board().getStones(ctx.pitIdx) == 0) {
            return ctx
        }

        val mancalaIdx = ctx.getPlayerMancalaIndex()
        var currentPitIdx = ctx.pitIdx

        var stonesToDistribute = ctx.board().getStones(ctx.pitIdx)

        val pits = mutableListOf<Int>()

        while (stonesToDistribute > 0) {
            currentPitIdx = (currentPitIdx + 1) % ctx.board().getTotalPits()

            if (ctx.board().isMancalaPit(currentPitIdx) && currentPitIdx != mancalaIdx) {
                continue
            }

            pits.add(currentPitIdx)
            stonesToDistribute -= 1
        }

        val updatedBoard = ctx.board()
            .emptyPit(ctx.pitIdx)
            .addStoneToPits(pits)

        return ctx.withBoard(updatedBoard)
            .withLastPitIdx(currentPitIdx)
    }
}
