package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.GameState
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Game rule for determining if the game is over.
 * This rule checks if the game is over, collects all stones into Mancalas, and determines the winner.
 *
 * he game is considered over if all pits are empty for all but one player.
 */
@Component
@Order(30)
object GameOverGameRule : GameRule {
    override fun apply(ctx: GameContext): GameContext {
        if (isGameOver(ctx)) {
            val updatedBoard = ctx.board().collectAllStones()

            val maybeWinner = determineWinner(updatedBoard, ctx.game.totalPlayers)

            return ctx.withBoard(updatedBoard)
                .withGameState(GameState.GAME_OVER)
                .withWinner(maybeWinner)
        }

        return ctx
    }

    private fun isGameOver(ctx: GameContext) =
        (0 until ctx.game.totalPlayers)
            .map { ctx.board().getPitsInRow(it).all { p -> p == 0 } }
            .filter { it }
            .size >= ctx.game.totalPlayers - 1

    private fun determineWinner(board: Board, totalPlayers: Int): Int? {
        val scores = (0 until totalPlayers).associateWith {
            board.getStones(board.getRowMancalaPit(it))
        }

        val maxScore = scores.maxByOrNull { it.value }?.value
        val winners = scores.filterValues { it == maxScore }.keys

        return if (winners.size == 1) winners.first() else null // null in case of a draw
    }
}