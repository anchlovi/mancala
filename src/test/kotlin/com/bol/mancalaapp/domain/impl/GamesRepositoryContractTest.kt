package com.bol.mancalaapp.domain.impl

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.GameNotFoundException
import com.bol.mancalaapp.domain.GamesRepository
import com.bol.mancalaapp.domain.VersionMismatchException
import com.bol.mancalaapp.helpers.GamesHelper
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


abstract class GamesRepositoryContractTest {

    private lateinit var gamesRepository: GamesRepository

    // This method needs to be implemented in concrete test classes to provide the specific repository implementation.
    protected abstract fun createRepository(): GamesRepository

    @BeforeEach
    fun setUp() {
        gamesRepository = createRepository()
    }

    @Test
    fun `create should add a game and findById should retrieve it`(): Unit = runBlocking {
        val game = GamesHelper.newGame()
        gamesRepository.create(game).asDeferred().await()

        val retrievedGame = gamesRepository.findById(game.id).asDeferred().await()
        assertEquals(game, retrievedGame)
    }

    @Test
    fun `findById should throw GameNotFoundException for unknown id`(): Unit = runBlocking {
        val unknownId = GameId.randomUUID()

        assertThrows<GameNotFoundException> {
            gamesRepository.findById(unknownId).asDeferred().await()
        }
    }

    @Test
    fun `update should change game data and increment version`(): Unit = runBlocking {
        val game = GamesHelper.newGame().copy(version = 0)
        gamesRepository.create(game).asDeferred().await()

        val updatedGame = game.copy(currentPlayer = game.nextPlayer())
        gamesRepository.update(updatedGame).asDeferred().await()

        val retrievedGame = gamesRepository.findById(game.id).asDeferred().await()
        assertEquals(updatedGame.copy(version = updatedGame.version + 1), retrievedGame)
    }

    @Test
    fun `update should throw VersionMismatchException on version conflict`(): Unit = runBlocking {
        val game = GamesHelper.newGame()
        gamesRepository.create(game).asDeferred().await()

        val updatedGame = game.copy(version = game.version + 2)
        assertThrows<VersionMismatchException> {
            gamesRepository.update(updatedGame).asDeferred().await()
        }
    }

    @Test
    fun `findByIdAndVersion should retrieve correct version of game`(): Unit = runBlocking {
        val game = GamesHelper.newGame().copy(version = 0)
        gamesRepository.create(game).asDeferred().await()

        val retrievedGame = gamesRepository.findByIdAndVersion(game.id, game.version).asDeferred().await()
        assertEquals(game, retrievedGame)
    }

    @Test
    fun `findByIdAndVersion should throw GameNotFoundException for incorrect version`(): Unit = runBlocking {
        val game = GamesHelper.newGame()
        gamesRepository.create(game).asDeferred().await()

        assertThrows<GameNotFoundException> {
            gamesRepository.findByIdAndVersion(game.id, game.version + 1).asDeferred().await()
        }
    }
}
