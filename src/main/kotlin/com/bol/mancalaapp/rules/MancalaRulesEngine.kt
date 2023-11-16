package com.bol.mancalaapp.rules

import com.bol.mancalaapp.rules.validators.PlayerMoveValidator
import org.springframework.stereotype.Service

/**
 * A rules engine for the Mancala game that applies a series of game rules and validators to the game context.
 *
 * The engine first validates the current game context using a set of player move validators. It then applies
 * a sequence of game rules, each potentially modifying the game state. The final game context, reflecting
 * the cumulative effect of all the rules, is returned.
 *
 * @property rules The list of game rules to be applied to the game context.
 * @property validators The list of validators to check the validity of player moves.
 */
@Service
class MancalaRulesEngine(
    private val rules: List<GameRule>,
    private val validators: List<PlayerMoveValidator>
) : RulesEngine {

    /**
     * Applies a sequence of game rules to the provided game context.
     *
     * Each rule in the sequence may modify the game context. The method ensures that each rule
     * receives the most up-to-date version of the game context, reflecting the changes made by previous rules.
     * Before applying the rules, all the validators are executed to ensure the game context is in a valid state.
     *
     * @param ctx The initial game context before any rules are applied.
     * @return The updated game context after all rules have been applied.
     * @throws com.bol.mancalaapp.rules.validators.ValidationException If the game context fails validation.
     */
    override fun apply(ctx: GameContext): GameContext {
        validators.forEach { it.validate(ctx) }

        return rules.fold(ctx) { currentCtx, rule ->
            rule.apply(currentCtx)
        }
    }
}