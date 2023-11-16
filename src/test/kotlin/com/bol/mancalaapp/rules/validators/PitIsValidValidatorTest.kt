package com.bol.mancalaapp.rules.validators

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.helpers.GamesHelper
import com.bol.mancalaapp.rules.GameContext
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class PitIsValidValidatorTest {

    private lateinit var gameContext: GameContext

    @BeforeEach
    fun setUp() {
        val board = Board(pits = pits)
        gameContext = GamesHelper.newGameContext()
        gameContext = gameContext.copy(game = gameContext.game.copy(board = board))
    }

    @Test
    fun `should not throw exception when pit index is valid`() {
        val validPitIndex = (0 until gameContext.totalPits()).random()

        assertDoesNotThrow { PitIsValidValidator.validate(gameContext.copy(pitIdx = validPitIndex)) }
    }

    @ParameterizedTest
    @MethodSource("invalidPitIndicesProvider")
    fun `should throw exception when pit index is invalid`(invalidPitIndex: Int) {
        assertThrows<InvalidPitException> {
            PitIsValidValidator.validate(gameContext.copy(pitIdx = invalidPitIndex))
        }
    }

    companion object {
        val pits = listOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0)

        @JvmStatic
        fun invalidPitIndicesProvider(): List<Int> {
            val totalPits = pits.size
            return listOf(-1, -2, totalPits, totalPits + 1)
        }
    }
}
