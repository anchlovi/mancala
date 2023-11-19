package com.bol.mancalaapp.usecases.create

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.impl.InMemoryGamesRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class CreateNewGameUseCaseTest {
    private val gamesRepository = InMemoryGamesRepository()
    private val useCase = CreateNewGameUseCase(gamesRepository)

    @Test
    fun `should create new game`() {
        val command = CreateNewGameCommand(2,6, 4)

        val game = useCase.createNewGame(command)

        val expectedGame = Board.createBoard(command.totalPlayers, command.pitsForPlayer, command.stonesPerPit)

        assertEquals(expectedGame, game.board)
    }

    @Test
    fun `should call validate on command when creating new game`() {
        val command = mock<CreateNewGameCommand>()

        runBlocking {
            useCase.createNewGame(command)
        }

        verify(command).validate()
    }
}
