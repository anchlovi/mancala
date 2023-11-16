package com.bol.mancalaapp.api.v1.requests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CreateNewGameRequestTest {

    @Test
    fun `should correctly convert to CreateNewGameCommand`() {
        val numberOfPitsForPlayer = 6
        val numberOfStonesInEachPit = 4
        val request = CreateNewGameRequest(numberOfPitsForPlayer, numberOfStonesInEachPit)

        val command = request.toCommand()

        assertEquals(numberOfPitsForPlayer, command.numberOfPitsForPlayer)
        assertEquals(numberOfStonesInEachPit, command.numberOfStonesInEachPit)
    }
}