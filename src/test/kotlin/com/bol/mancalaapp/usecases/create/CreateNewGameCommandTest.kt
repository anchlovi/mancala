package com.bol.mancalaapp.usecases.create

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class CreateNewGameCommandTest {

    @ParameterizedTest
    @MethodSource("testDataProvider")
    fun `should throw exception when total players is less than 2`(cmd: CreateNewGameCommand, expectedMessage: String) {
        val exception = assertThrows<IllegalArgumentException> { cmd.validate() }
        assertEquals(expectedMessage, exception.message)
    }

    companion object {
        private val cmd = CreateNewGameCommand.DefaultCommand

        @JvmStatic
        fun testDataProvider(): List<Arguments> = listOf(
            Arguments.of(cmd.copy(totalPlayers = 1), "Total players must be greater than 1"),
            Arguments.of(cmd.copy(totalPlayers = 6), "Total players must be less than 6"),
            Arguments.of(cmd.copy(pitsForPlayer = 1), "Pits per player must be greater than 1"),
            Arguments.of(cmd.copy(pitsForPlayer = 15), "Pits per player must be less than 15"),
            Arguments.of(cmd.copy(stonesPerPit = 1), "Stones per pit must be greater than 1"),
            Arguments.of(cmd.copy(stonesPerPit = 20), "Stones per pit must be less than 20")
        )
    }
}