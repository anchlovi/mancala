package com.bol.mancalaapp.rules

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Game rule for determining the next player in a Mancala game.
 *
 * If the last stone lands in the player's own Mancala, and they have stones left to play, they get another turn.
 * Otherwise, the turn passes to the next player who has stones to play.
 */
@Component
@Order(40)
object DetermineNextPlayerGameRule : GameRule {
    override fun apply(ctx: GameContext): GameContext {
        if (ctx.game.gameState.isGameOver) {
            return ctx
        }

        val currentPlayerCanPlayAgain = ctx.lastPitIdx == ctx.getPlayerMancalaIndex() && ctx.hasStones(ctx.player())

        val nextPlayer = if (currentPlayerCanPlayAgain) {
            ctx.player()
        } else {
            ctx.findNextPlayablePlayer()
        }

        return ctx.withPlayer(nextPlayer)
    }

    private fun GameContext.findNextPlayablePlayer(): Int {
        val allPlayers = (0 until this.game.totalPlayers)
        val currentIndex = allPlayers.indexOf(this.player())
        val playersOrder = allPlayers.drop(currentIndex + 1) + allPlayers.take(currentIndex)

        return playersOrder.firstOrNull { this.hasStones(it) && it != this.player() } ?: allPlayers.first()
    }

    private fun GameContext.hasStones(player: Int): Boolean =
        this.game.board.getPitsInRow(player).sum() > 0
}
