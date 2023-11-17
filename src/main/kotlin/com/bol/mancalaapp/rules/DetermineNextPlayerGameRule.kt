package com.bol.mancalaapp.rules

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Game rule for determining the next player in a Mancala game.
 *
 * If the last stone lands in the player's own Mancala, the player gets another turn.
 * Otherwise, the turn switches to the opponent.
 */
@Component
@Order(30)
object DetermineNextPlayerGameRule : GameRule {
    override fun apply(ctx: GameContext): GameContext =
        when (ctx.lastPitIdx) {
            ctx.getPlayerMancalaIndex() -> ctx
            else -> ctx.withPlayer(ctx.game.nextPlayer())
    }
}