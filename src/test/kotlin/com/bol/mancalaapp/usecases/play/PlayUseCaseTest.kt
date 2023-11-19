package com.bol.mancalaapp.usecases.play

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameNotFoundException
import com.bol.mancalaapp.domain.GamesRepository
import com.bol.mancalaapp.domain.impl.InMemoryGamesRepository
import com.bol.mancalaapp.helpers.GamesHelper
import com.bol.mancalaapp.rules.GameContext
import com.bol.mancalaapp.rules.RulesEngine
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
    fun `play should update the game state correctly`() {
        val updatedGame = game.copy(currentPlayer = game.nextPlayer())
        val gameContext = GameContext(game, pitIndex)
        val updatedGameContext = gameContext.copy(game = updatedGame)

        whenever(rulesEngine.apply(gameContext)).thenReturn(updatedGameContext)

        val resultGame = useCase.play(PlayCommand(game.id, pitIndex, game.version))

        assertEquals(updatedGame.version + 1, resultGame.version)
        assertEquals(updatedGame.board, resultGame.board)
    }

    @Test
    fun `play should throw GameNotFoundException for unknown game id`() {
        val unknownGameId = GameId.randomUUID()

        assertThrows<GameNotFoundException> {
            useCase.play(PlayCommand(unknownGameId, pitIndex, game.version))
        }
    }

    @Test
    fun `should call validate on command`() {
        assertThrows<IllegalArgumentException> {
            useCase.play(PlayCommand(game.id, -1, game.version))
        }
    }
}
