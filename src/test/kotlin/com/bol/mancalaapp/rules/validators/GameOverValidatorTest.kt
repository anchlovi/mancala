package com.bol.mancalaapp.rules.validators

import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.helpers.GamesHelper
import com.bol.mancalaapp.rules.GameContext
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class GameOverValidatorTest {

    private lateinit var gameContext: GameContext

    @BeforeEach
    fun setUp() {
        gameContext = GamesHelper.newGameContext()
    }

    @Test
    fun `should not throw exception when game is not over`() {
        val gameInProgressContext = gameContext.copy(
            game = gameContext.game.copy(gameState = GameState.IN_PROGRESS))

        assertDoesNotThrow { GameOverValidator.validate(gameInProgressContext) }
    }

    @Test
    fun `should throw exception when game is over`() {
        val gameOverContext = gameContext.copy(
            game = gameContext.game.copy(gameState = GameState.GAME_OVER))

        // Act and Assert
        assertThrows<GameIsOverException> { GameOverValidator.validate(gameOverContext) }
    }
}