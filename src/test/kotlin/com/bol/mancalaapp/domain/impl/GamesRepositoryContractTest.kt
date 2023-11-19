package com.bol.mancalaapp.domain.impl

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.GameNotFoundException
import com.bol.mancalaapp.domain.GamesRepository
import com.bol.mancalaapp.domain.VersionMismatchException
import com.bol.mancalaapp.helpers.GamesHelper
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
    fun `create should add a game and findById should retrieve it`() {
        val game = GamesHelper.newGame()
        gamesRepository.create(game)

        val retrievedGame = gamesRepository.findById(game.id)
        assertEquals(game, retrievedGame)
    }

    @Test
    fun `findById should throw GameNotFoundException for unknown id`() {
        val unknownId = GameId.randomUUID()

        assertThrows<GameNotFoundException> {
            gamesRepository.findById(unknownId)
        }
    }

    @Test
    fun `update should change game data and increment version`() {
        val game = GamesHelper.newGame().copy(version = 0)
        gamesRepository.create(game)

        val updatedGame = game.copy(currentPlayer = game.nextPlayer())
        gamesRepository.update(updatedGame)

        val retrievedGame = gamesRepository.findById(game.id)
        assertEquals(updatedGame.copy(version = updatedGame.version + 1), retrievedGame)
    }

    @Test
    fun `update should throw VersionMismatchException on version conflict`() {
        val game = GamesHelper.newGame()
        gamesRepository.create(game)

        val updatedGame = game.copy(version = game.version + 2)
        assertThrows<VersionMismatchException> {
            gamesRepository.update(updatedGame)
        }
    }

    @Test
    fun `findByIdAndVersion should retrieve correct version of game`() {
        val game = GamesHelper.newGame().copy(version = 0)
        gamesRepository.create(game)

        val retrievedGame = gamesRepository.findByIdAndVersion(game.id, game.version)
        assertEquals(game, retrievedGame)
    }

    @Test
    fun `findByIdAndVersion should throw GameNotFoundException for incorrect version`() {
        val game = GamesHelper.newGame()
        gamesRepository.create(game)

        assertThrows<GameNotFoundException> {
            gamesRepository.findByIdAndVersion(game.id, game.version + 1)
        }
    }
}
