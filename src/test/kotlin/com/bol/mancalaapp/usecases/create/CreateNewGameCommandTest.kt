package com.bol.mancalaapp.usecases.create

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CreateNewGameCommandTest {

    @Test
    fun `should correctly calculate total number of pits`() {
        val numberOfPitsForPlayer = 6
        val numberOfStonesInEachPit = 4

        val command = CreateNewGameCommand(numberOfPitsForPlayer, numberOfStonesInEachPit)

        val expectedTotalNumberOfPits = 14
        val totalNumberOfPits = command.totalNumberOfPits()

        assertEquals(expectedTotalNumberOfPits, totalNumberOfPits)
    }
}