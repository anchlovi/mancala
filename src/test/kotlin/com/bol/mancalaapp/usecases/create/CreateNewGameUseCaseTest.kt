package com.bol.mancalaapp.usecases.create

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.domain.Player
import com.bol.mancalaapp.domain.impl.InMemoryGamesRepository
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class CreateNewGameUseCaseTest {

    @Test
    fun `should create new game`(): Unit = runBlocking {
        val gamesRepository = InMemoryGamesRepository()
        val useCase = CreateNewGameUseCase(gamesRepository)
        val command = CreateNewGameCommand(6, 4)

        // Act
        val game = useCase.createNewGame(command).asDeferred().await()

        // Assert
        assertEquals(GameState.IN_PROGRESS, game.gameState)
        assertEquals(Player.PLAYER1, game.currentPlayer)
        assertEquals(0, game.version)
        assertEquals(command.totalNumberOfPits(), game.board.pits.size)

        assertTrue(Board.getPitsForPlayer(Player.PLAYER1, game.board.pits).all { game.board.pits[it] == 4 })
        assertTrue(Board.getPitsForPlayer(Player.PLAYER2, game.board.pits).all { game.board.pits[it] == 4 })

        assertEquals(0, game.board.pits[Board.getPlayerMancalaIndex(Player.PLAYER1, command.numberOfPitsForPlayer)])
        assertEquals(0, game.board.pits[Board.getPlayerMancalaIndex(Player.PLAYER2, command.numberOfPitsForPlayer)])
    }
}