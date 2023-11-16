package com.bol.mancalaapp.rules

/**
 * Represents a single rule in the Mancala game.
 *
 * This interface defines the contract for game rules, where each rule can modify the game context based on
 * specific game logic. Implementations of this interface should encapsulate the logic for a single aspect of the game,
 * such as distributing stones, capturing stones, determining the next player, or checking the game's end condition.
 */
interface GameRule {
    /**
     * Applies the rule to the given game context.
     *
     * This method takes the current state of the game, encapsulated in `ctx`, applies the rule's logic,
     * and returns the updated game context. Implementations may modify various aspects of the game,
     * such as the board state, the current player, or the game status.
     *
     * @param ctx The current game context.
     * @return The updated game context after applying the rule.
     */
    fun apply(ctx: GameContext): GameContext
}
