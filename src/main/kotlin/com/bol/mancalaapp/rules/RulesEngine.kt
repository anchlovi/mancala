package com.bol.mancalaapp.rules

/**
 * Defines the interface for a rules engine in the Mancala game.
 *
 * A rules engine orchestrates the application of a set of game rules and validations to the current game context.
 * It is responsible for processing the game context through validations and various game rules,
 * each potentially altering the game state based on specific conditions and logic.
 * The result is an updated game context that reflects the cumulative effect of all applied rules and validations.
 *
 * Validations are typically performed prior to applying the rules to ensure that the game context
 * is in a valid state suitable for rule processing. These validations may include checking the current game status,
 * verifying player turns, or ensuring the selected pit is valid.
 */
interface RulesEngine {
    /**
     * Applies a series of game rules and validations to the provided game context.
     *
     * This method first performs necessary validations to ensure the integrity and validity of the game context.
     * It then processes the game context through each game rule in sequence. Each rule can modify and return
     * a new version of the game context. The method ensures that subsequent rules receive the most current state
     * of the game context, allowing for accurate and consistent application of both rules and validations.
     *
     * @param ctx The initial game context representing the current state of the game.
     * @return The updated game context after all validations and rules have been applied.
     * @throws com.bol.mancalaapp.rules.validators.ValidationException If the game context fails validation.
     */
    fun apply(ctx: GameContext): GameContext
}
