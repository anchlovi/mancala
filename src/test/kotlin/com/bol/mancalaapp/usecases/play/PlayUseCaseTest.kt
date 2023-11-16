package com.bol.mancalaapp.usecases.play

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameNotFoundException
import com.bol.mancalaapp.domain.GamesRepository
import com.bol.mancalaapp.domain.impl.InMemoryGamesRepository
import com.bol.mancalaapp.helpers.GamesHelper
import com.bol.mancalaapp.rules.GameContext
import com.bol.mancalaapp.rules.RulesEngine
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class PlayUseCaseTest {

    private lateinit var gamesRepository: GamesRepository
    private lateinit var rulesEngine: RulesEngine
    private lateinit var useCase: PlayUseCase
    private lateinit var game: Game
    private val pitIndex = 3

    @BeforeEach
    fun setUp() {
        gamesRepository = InMemoryGamesRepository()
        rulesEngine = mock()
        useCase = PlayUseCase(gamesRepository, rulesEngine)

        game = GamesHelper.newGame()
        gamesRepository.create(game)
    }

    @Test
    fun `play should update the game state correctly`(): Unit = runBlocking {
        val updatedGame = game.copy(currentPlayer = game.currentPlayer.opponent)
        val gameContext = GameContext(game, pitIndex)
        val updatedGameContext = gameContext.copy(game = updatedGame)

        whenever(rulesEngine.apply(gameContext)).thenReturn(updatedGameContext)

        val resultGame = useCase.play(PlayCommand(game.id, pitIndex, game.version)).asDeferred().await()

        assertEquals(updatedGame.version + 1, resultGame.version)
        assertEquals(updatedGame.board, resultGame.board)
    }

    @Test
    fun `play should throw GameNotFoundException for unknown game id`(): Unit = runBlocking {
        val unknownGameId = GameId.randomUUID()

        assertThrows<GameNotFoundException> {
            useCase.play(PlayCommand(unknownGameId, pitIndex, game.version)).asDeferred().await()
        }
    }
}
