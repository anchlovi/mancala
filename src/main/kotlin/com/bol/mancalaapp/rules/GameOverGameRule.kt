package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.domain.Player
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Game rule for determining if the game is over.
 * This rule checks if the game is over, collects all stones into Mancalas, and determines the winner.
 */
@Component
@Order(40)
object GameOverGameRule : GameRule {
    override fun apply(ctx: GameContext): GameContext {
        if (isGameOver(ctx.board())) {
            val updatedBoard = ctx.board().collectAllStones()

            val maybeWinner = determineWinner(updatedBoard)

            return ctx.withBoard(updatedBoard)
                .withGameState(GameState.GAME_OVER)
                .withWinner(maybeWinner)
        }

        return ctx
    }

    private fun isGameOver(board: Board) =
        Player.entries.any { board.getPitsInRow(it.ordinal).all { p -> p == 0 } }

    private fun determineWinner(board: Board): Player? {
        val scores = Player.entries.associateWith {
            board.getStones(board.getRowMancalaPit(it.ordinal))
        }

        val maxScore = scores.maxByOrNull { it.value }?.value
        val winners = scores.filterValues { it == maxScore }.keys

        return if (winners.size == 1) winners.first() else null // null in case of a draw
    }
}