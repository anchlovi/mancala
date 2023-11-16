package com.bol.mancalaapp.api.v1

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.api.v1.exceptions.ErrorResponse
import com.bol.mancalaapp.api.v1.requests.CreateNewGameRequest
import com.bol.mancalaapp.config.JacksonConfig
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameNotFoundException
import com.bol.mancalaapp.domain.VersionMismatchException
import com.bol.mancalaapp.helpers.GamesHelper
import com.bol.mancalaapp.usecases.create.CreateNewGameUseCase
import com.bol.mancalaapp.usecases.find.FindGameByIdUseCase
import com.bol.mancalaapp.usecases.play.PlayCommand
import com.bol.mancalaapp.usecases.play.PlayUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.concurrent.CompletableFuture

@WebMvcTest(GamesController::class)
@AutoConfigureMockMvc
@Import(JacksonConfig::class)
class GamesControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var createUseCase: CreateNewGameUseCase

    @MockBean
    private lateinit var findGameByIdUseCase: FindGameByIdUseCase

    @MockBean
    private lateinit var playUseCase: PlayUseCase

    private lateinit var game: Game

    @BeforeEach
    fun setup() {
        game = GamesHelper.newGame()
    }

    @Test
    fun `create should return 201 when a new game is created`() {
        val request = CreateNewGameRequest(
            numberOfPitsForPlayer = 4,
            numberOfStonesInEachPit = 4,
        )

        whenever(createUseCase.createNewGame(request.toCommand()))
            .thenReturn(CompletableFuture.completedStage(game))

        val results = mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(createNewGame(request)))
            .andExpect(MockMvcResultMatchers.status().isCreated)

        assertGameProperties(results, game)
    }

    @Test
    fun `findGameById should return 200 and board data when board is found`() {
        whenever(findGameByIdUseCase.findGame(game.id))
            .thenReturn(CompletableFuture.completedFuture(game))

        val results = mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(findById(game.id)))
            .andExpect(MockMvcResultMatchers.status().isOk)

        assertGameProperties(results, game)
    }

    @Test
    fun `findById should return 404 when game is not found`() {
        whenever(findGameByIdUseCase.findGame(game.id))
            .thenReturn(CompletableFuture.failedStage(GameNotFoundException(game.id)))

        assertGameWasNotFound(findById(game.id), game.id)
    }

    @Test
    fun `play should return 200 when move is valid`() {
        val command = PlayCommand(game.id, 1, game.version)

        whenever(playUseCase.play(command))
            .thenReturn(CompletableFuture.completedStage(game))

        val results = mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(play(command)))
            .andExpect(MockMvcResultMatchers.status().isOk)

        assertGameProperties(results, game)
    }

    @Test
    fun `play should return 404 when game is not found`() {
        val command = PlayCommand(game.id, 1, 1)

        whenever(playUseCase.play(command))
            .thenReturn(CompletableFuture.failedStage(GameNotFoundException(command.gameId)))

        assertGameWasNotFound(play(command), command.gameId)
    }

    @Test
    fun `play should return 429 on game version conflicts`() {
        val command = PlayCommand(game.id, 1, 1)

        whenever(playUseCase.play(command))
            .thenReturn(CompletableFuture.failedStage(VersionMismatchException()))

        assertVersionMismatch(play(command))
    }

    private fun createNewGame(body: CreateNewGameRequest): MvcResult {
        return mockMvc.perform(
            MockMvcRequestBuilders.post(GAMES_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body))
                .characterEncoding("utf-8")
        ).andReturn()
    }

    private fun findById(gameId: GameId): MvcResult {
        return mockMvc.get("${GAMES_URI}/${gameId}").andReturn()
    }

    private fun play(cmd: PlayCommand): MvcResult =
        mockMvc.put("${GAMES_URI}/${cmd.gameId}/play/${cmd.pitIdx}?version=${cmd.version}").andReturn()

    private fun assertGameWasNotFound(mvcResult: MvcResult, gameId: GameId) =
        assertError(
            mvcResult = mvcResult,
            expectedStatus = HttpStatus.NOT_FOUND,
            expectedError = ErrorResponse(
                type = GameNotFoundException::class.qualifiedName,
                message = "Game with id [$gameId] not found"
            )
        )

    private fun assertVersionMismatch(mvcResult: MvcResult) =
        assertError(
            mvcResult = mvcResult,
            expectedStatus = HttpStatus.CONFLICT,
            expectedError = ErrorResponse(
                type = VersionMismatchException::class.qualifiedName,
                message = "Version mismatch"
            )
        )

    private fun assertError(mvcResult: MvcResult, expectedStatus: HttpStatus, expectedError: ErrorResponse) {
        mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(mvcResult))
            .andExpect(MockMvcResultMatchers.status().`is`(expectedStatus.value()))
            .andExpectAll(
                MockMvcResultMatchers.jsonPath("$.type").value(expectedError.type),
                MockMvcResultMatchers.jsonPath("$.message").value(expectedError.message)
            )
    }

    private fun assertGameProperties(resultActions: ResultActions, expectedGame: Game) {
        resultActions.andExpectAll(
            MockMvcResultMatchers.jsonPath("$.id").value(expectedGame.id.toString()),
            MockMvcResultMatchers.jsonPath("$.board.pits").value(expectedGame.board.pits),
            MockMvcResultMatchers.jsonPath("$.board.pits_for_player").value(expectedGame.board.pitsForPlayer),
            MockMvcResultMatchers.jsonPath("$.current_player").value(expectedGame.currentPlayer.name),
            MockMvcResultMatchers.jsonPath("$.game_state").value(expectedGame.gameState.name),
            MockMvcResultMatchers.jsonPath("$.winner").value(expectedGame.winner?.name),
            MockMvcResultMatchers.jsonPath("$.version").value(expectedGame.version)
        )
    }

    companion object {
        private const val GAMES_URI = "/api/v1/games"
    }
}