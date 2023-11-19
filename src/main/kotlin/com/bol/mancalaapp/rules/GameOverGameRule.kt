package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.GameState
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

/**
 * Game rule for determining if the game is over.
 *
 * The game is considered over if all pits are empty for all but one player.
 */
@Component
@Order(30)
object GameOverGameRule : GameRule {
    override fun apply(ctx: GameContext): GameContext {
        if (isGameOver(ctx)) {
            return ctx.withGameState(GameState.GAME_OVER)
        }

        return ctx
    }

    private fun isGameOver(ctx: GameContext) =
        (0 until ctx.game.totalPlayers)
            .map { ctx.board().getPitsInRow(it).sum() }
            .filter { it > 0}
            .size < 2 // need at least 2 players to continue the game
}