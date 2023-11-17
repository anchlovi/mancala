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
            val opponentPitIdx = ctx.board().getOppositePitIndex(ctx.lastPitIdx)
            val mancalaIdx = ctx.getPlayerMancalaIndex()


            val updatedBoard = ctx.board()
                .moveStones(opponentPitIdx, mancalaIdx)
                .moveStones(ctx.lastPitIdx, mancalaIdx)

            return ctx.withBoard(updatedBoard)
        }

        return ctx
    }

    private fun captureRuleMet(ctx: GameContext): Boolean =
        ctx.board().getStones(ctx.lastPitIdx) == 1 &&
                ctx.board().getStones(ctx.board().getOppositePitIndex(ctx.lastPitIdx)) > 0 &&
                ctx.isPitOwnedByCurrentPlayer(ctx.lastPitIdx) &&
                ctx.getPlayerMancalaIndex() != ctx.lastPitIdx
}