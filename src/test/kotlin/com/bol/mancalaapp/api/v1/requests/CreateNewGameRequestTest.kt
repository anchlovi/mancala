package com.bol.mancalaapp.api.v1.requests

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CreateNewGameRequestTest {

    @Test
    fun `should correctly convert to CreateNewGameCommand`() {
        val totalPlayers = 2
        val pitsForPlayer = 6
        val stonesPerPit = 4
        val request = CreateNewGameRequest(totalPlayers, pitsForPlayer, stonesPerPit)

        val command = request.toCommand()

        assertEquals(totalPlayers, command.totalPlayers)
        assertEquals(pitsForPlayer, command.pitsForPlayer)
        assertEquals(stonesPerPit, command.stonesPerPit)
    }
}