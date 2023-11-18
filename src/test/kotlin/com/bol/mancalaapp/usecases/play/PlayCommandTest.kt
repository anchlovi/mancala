package com.bol.mancalaapp.usecases.play

import com.bol.mancalaapp.GameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class PlayCommandTest {

    @ParameterizedTest
    @MethodSource("testDataProvider")
    fun `should throw exception when play command is invalid`(cmd: PlayCommand, expectedMessage: String) {
        val exception = assertThrows<IllegalArgumentException> { cmd.validate() }
        assertEquals(expectedMessage, exception.message)
    }

    companion object {
        private val cmd = PlayCommand(
            gameId = GameId.randomUUID(),
            pitIdx = 1,
            version = 1
        )

        @JvmStatic
        fun testDataProvider(): List<Arguments> = listOf(
            Arguments.of(cmd.copy(pitIdx = -1), "Pit index (pitIdx) must be non-negative"),
            Arguments.of(cmd.copy(version = -1), "Version must be non-negative"),
        )
    }
}
