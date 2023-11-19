package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Game rule for determining the winner of the game if any.
 */
@Component
@Order(50)
object DetermineWinnerGameRule : GameRule {
    override fun apply(ctx: GameContext): GameContext {
        if (ctx.game.gameState.isGameOver.not()) {
            return ctx
        }

        val maybeWinner = determineWinner(ctx.board(), ctx.game.totalPlayers)

        return ctx.withWinner(maybeWinner)
    }

    private fun determineWinner(board: Board, totalPlayers: Int): Int? {
        val scores = (0 until totalPlayers).associateWith {
            board.getStones(board.getRowMancalaPit(it))
        }

        val maxScore = scores.maxByOrNull { it.value }?.value
        val winners = scores.filterValues { it == maxScore }.keys

        return if (winners.size == 1) winners.first() else null // null in case of a draw
    }
}