package com.bol.mancalaapp.rules.validators

import com.bol.mancalaapp.helpers.GamesHelper
import com.bol.mancalaapp.rules.GameContext
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EmptyPitValidatorTest {

    private lateinit var gameContext: GameContext

    @BeforeEach
    fun setUp() {
        val pits = listOf(4, 0, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0)

        gameContext = GamesHelper.newGameContext().withPits(pits = pits)
    }

    @Test
    fun `should not throw exception when pit is not empty`() {
        val notEmptyPitIndex = 0

        assertDoesNotThrow { EmptyPitValidator.validate(gameContext.copy(pitIdx = notEmptyPitIndex)) }
    }

    @Test
    fun `should throw exception when pit is empty`() {
        val emptyPitIndex = 1

        assertThrows<PitHasNoStonesException> {
            EmptyPitValidator.validate(gameContext.copy(pitIdx = emptyPitIndex))
        }
    }
}
