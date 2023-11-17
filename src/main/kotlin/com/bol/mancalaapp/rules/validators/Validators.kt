package com.bol.mancalaapp.rules.validators

import com.bol.mancalaapp.rules.GameContext
import org.springframework.stereotype.Component

/**
 * An interface for validators used to validate player moves in the Mancala game.
 */
interface PlayerMoveValidator {
    /**
     * Validates a player's move in the given game context.
     *
     * @param ctx The current game context.
     * @throws ValidationException If the validation fails.
     */
    fun validate(ctx: GameContext)
}

/**
 * Validator to check that a selected pit is not empty.
 */
@Component
object EmptyPitValidator : PlayerMoveValidator {
    /**
     * Validates that the selected pit is not empty.
     *
     * @param ctx The current game context.
     * @throws PitHasNoStonesException If the selected pit has no stones.
     */
    override fun validate(ctx: GameContext) {
        if (ctx.board().getStones(ctx.pitIdx) == 0) {
            throw PitHasNoStonesException(ctx.pitIdx)
        }
    }
}

/**
 * Validator to check that a selected pit is not a Mancala pit.
 */
@Component
object PitIsMancalaValidator : PlayerMoveValidator {
    /**
     * Validates that the selected pit is not a Mancala pit.
     *
     * @param ctx The current game context.
     * @throws PitIsMancalaException If the selected pit is a Mancala pit.
     */
    override fun validate(ctx: GameContext) {
        if (ctx.game.board.isMancalaPit(ctx.pitIdx)) {
            throw PitIsMancalaException(ctx.pitIdx)
        }
    }
}

/**
 * Validator to check that the selected pit belongs to the current player.
 */
@Component
object PitBelongsToPlayerValidator : PlayerMoveValidator {
    /**
     * Validates that the selected pit belongs to the current player.
     *
     * @param ctx The current game context.
     * @throws PitDoesNotBelongToPlayerException If the pit does not belong to the current player.
     */
    override fun validate(ctx: GameContext) {
        if (ctx.isPitOwnedByCurrentPlayer(ctx.pitIdx).not()) {
            throw PitDoesNotBelongToPlayerException(ctx.pitIdx, ctx.player())
        }
    }
}

/**
 * Validator to check that the selected pit index is valid.
 */
@Component
object PitIsValidValidator : PlayerMoveValidator {
    /**
     * Validates that the selected pit index is within the valid range.
     *
     * @param ctx The current game context.
     * @throws InvalidPitException If the pit index is out of range.
     */
    override fun validate(ctx: GameContext) {
        if (ctx.pitIdx < 0 || ctx.pitIdx >= ctx.board().getTotalPits()) {
            throw InvalidPitException(ctx.pitIdx)
        }
    }
}

/**
 * Validator to check that the game is not over.
 */
@Component
object GameOverValidator : PlayerMoveValidator {
    /**
     * Validates that the game is still in progress.
     *
     * @param ctx The current game context.
     * @throws GameIsOverException If the game has already ended.
     */
    override fun validate(ctx: GameContext) {
        if (ctx.gameState().isGameOver) {
            throw GameIsOverException(ctx.game.id)
        }
    }
}