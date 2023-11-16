package com.bol.mancalaapp.usecases.find

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameNotFoundException
import com.bol.mancalaapp.domain.GamesRepository
import com.bol.mancalaapp.domain.impl.InMemoryGamesRepository
import com.bol.mancalaapp.helpers.GamesHelper
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class FindGameByIdUseCaseTest {

    private lateinit var gamesRepository: GamesRepository
    private lateinit var useCase: FindGameByIdUseCase
    private lateinit var game: Game


    @BeforeEach
    fun setUp() {
        gamesRepository = InMemoryGamesRepository()
        useCase = FindGameByIdUseCase(gamesRepository)

        game = GamesHelper.newGame()

        gamesRepository.create(game)
    }

    @Test
    fun `findGame should retrieve the correct game`(): Unit = runBlocking {
        val retrievedGame = useCase.findGame(game.id).asDeferred().await()
        assertEquals(game, retrievedGame)
    }

    @Test
    fun `findGame should throw GameNotFoundException for unknown id`(): Unit = runBlocking {
        val unknownId = GameId.randomUUID()

        assertThrows<GameNotFoundException> {
            useCase.findGame(unknownId).asDeferred().await()
        }
    }
}