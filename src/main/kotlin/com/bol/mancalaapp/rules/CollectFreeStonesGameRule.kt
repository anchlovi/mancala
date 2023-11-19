package com.bol.mancalaapp.rules

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Game rule for collecting all stones from the board to their respective Mancalas if the game is over.
 */
@Component
@Order(40)
object CollectFreeStonesGameRule : GameRule {
    override fun apply(ctx: GameContext): GameContext {
        if (ctx.game.gameState.isGameOver.not()) {
            return ctx
        }

        return ctx.withBoard(ctx.board().collectAllStones());
    }
}