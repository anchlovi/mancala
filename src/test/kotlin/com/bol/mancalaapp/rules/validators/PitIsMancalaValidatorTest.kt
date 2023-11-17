package com.bol.mancalaapp.rules.validators

import com.bol.mancalaapp.helpers.GamesHelper
import com.bol.mancalaapp.rules.GameContext
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PitIsMancalaValidatorTest {

    private lateinit var gameContext: GameContext
    private var mancalaPitIndex: Int = -1
    private var notMancalaPitIndex: Int = -1

    @BeforeEach
    fun setUp() {
        gameContext = GamesHelper.newGameContext()
        mancalaPitIndex = gameContext.getPlayerMancalaIndex(player = 1)
        notMancalaPitIndex = mancalaPitIndex - 1
    }

    @Test
    fun `should not throw exception when pit is not a Mancala pit`() {
        assertDoesNotThrow { PitIsMancalaValidator.validate(gameContext.copy(pitIdx = notMancalaPitIndex)) }
    }

    @Test
    fun `should throw exception when pit is a Mancala pit`() {
        assertThrows<PitIsMancalaException> {
            PitIsMancalaValidator.validate(gameContext.copy(pitIdx = mancalaPitIndex))
        }
    }
}