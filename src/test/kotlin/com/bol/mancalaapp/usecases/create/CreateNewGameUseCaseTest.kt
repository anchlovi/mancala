package com.bol.mancalaapp.usecases.create

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.impl.InMemoryGamesRepository
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CreateNewGameUseCaseTest {

    @Test
    fun `should create new game`(): Unit = runBlocking {
        val gamesRepository = InMemoryGamesRepository()
        val useCase = CreateNewGameUseCase(gamesRepository)
        val command = CreateNewGameCommand(2,6, 4)

        val game = useCase.createNewGame(command).asDeferred().await()

        val expectedGame = Board.createBoard(command.totalPlayers, command.pitsForPlayer, command.stonesPerPit)

        assertEquals(expectedGame, game.board)
    }
}