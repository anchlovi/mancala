package com.bol.mancalaapp.api.v1.requests

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.usecases.play.PlayCommand
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PlayRequestTest {

    @Test
    fun `should correctly convert to PlayCommand`() {
        val gameId = GameId.randomUUID()
        val pitIndex = 1
        val version = 0
        val playRequest = PlayRequest(pitIndex, version)

        val playCommand = playRequest.toCommand(gameId)

        assertEquals(PlayCommand(gameId, pitIndex, version), playCommand)
    }
}