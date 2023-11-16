package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.domain.Player
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Game rule for determining the game state at the end of a Mancala game.
 * This rule checks if the game is over, collects all stones into Mancalas, and determines the winner.
 */
@Component
@Order(40)
object DetermineGameStateGameRule : GameRule {
    override fun apply(ctx: GameContext): GameContext {
        if (isGameOver(ctx.pits())) {
            val pits = ctx.pits().toMutableList()

            collectAllStones(pits, ctx.pitsForPlayer())
            emptyPits(pits, ctx.pitsForPlayer())

            val maybeWinner = determineWinner(pits, ctx.pitsForPlayer())

            return ctx.withPits(pits)
                .withGameState(GameState.GAME_OVER)
                .withWinner(maybeWinner)
        }

        return ctx
    }

    private fun isGameOver(pits: List<Int>) =
        Player.entries.any { Board.getPitsForPlayer(it, pits).all { p -> p == 0 } }

    private fun collectAllStones(pits: MutableList<Int>, pitsForPlayer: Int) =
        Player.entries.forEach {
            pits[Board.getPlayerMancalaIndex(it, pitsForPlayer)] += Board.getPitsForPlayer(it, pits).sum()
        }

    private fun emptyPits(pits: MutableList<Int>, pitsForPlayer: Int) =
        pits.indices.forEach {
            if (Board.isNotMancalaPit(it, pitsForPlayer)) pits[it] = 0
        }

    private fun determineWinner(pits: List<Int>, pitsForPlayer: Int): Player? {
        val scores = Player.entries.associateWith {
            pits[Board.getPlayerMancalaIndex(it, pitsForPlayer)]
        }

        val maxScore = scores.maxByOrNull { it.value }?.value
        val winners = scores.filterValues { it == maxScore }.keys

        return if (winners.size == 1) winners.first() else null // null in case of a draw
    }

}