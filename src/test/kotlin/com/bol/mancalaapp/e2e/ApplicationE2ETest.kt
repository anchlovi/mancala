package com.bol.mancalaapp.e2e

import com.bol.mancalaapp.api.v1.requests.CreateNewGameRequest
import com.bol.mancalaapp.api.v1.requests.PlayRequest
import com.bol.mancalaapp.api.v1.responses.BoardResponse
import com.bol.mancalaapp.api.v1.responses.GameResponse
import com.bol.mancalaapp.config.JacksonConfig
import com.bol.mancalaapp.db.ApplicationShutDownConfiguration
import com.bol.mancalaapp.db.DBTestConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.testcontainers.junit.jupiter.Testcontainers
import java.net.URL

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(DBTestConfiguration::class, ApplicationShutDownConfiguration::class, JacksonConfig::class)
class ApplicationE2ETest {

    @LocalServerPort
    private val port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun `run 2 players with 3 pits each and 2 stones in each pit`() {
        val scenario = mapper.readValue(loadScenario("game_2_3_3"), Scenario::class.java)

        val game = createGame(scenario)

        scenario.gameSteps.fold(game) { currentGame, step ->
            play(currentGame, step)
        }
    }

    private fun createGame(scenario: Scenario): GameResponse {
        val response = restTemplate.postForEntity(
            "http://localhost:$port/api/v1/games",
            CreateNewGameRequest(
                totalPlayers = scenario.gameSetup.totalPlayers,
                pitsForPlayer = scenario.gameSetup.pitsPerPlayer,
                stonesPerPit = scenario.gameSetup.stonesPerPit
            ), GameResponse::class.java)

        assert(response.statusCode.is2xxSuccessful)

        val gameResponse = response.body!!

        val expectedBoard = BoardResponse(pits = scenario.initialState.pits, pitsForPlayer = scenario.gameSetup.pitsPerPlayer)

        assertEquals(expectedBoard, gameResponse.board)

        return gameResponse
    }

    private fun play(game: GameResponse, step: GameStep): GameResponse {
        val playResponse = restTemplate.exchange(
            "http://localhost:$port/api/v1/games",
            HttpMethod.PUT,
            HttpEntity(PlayRequest(
                gameId = game.id,
                pitIndex = step.pitIndex,
                version = game.version
            )),
            GameResponse::class.java
        )

        assert(playResponse.statusCode.is2xxSuccessful)

        val currentGame = playResponse.body!!

        assertEquals(step.expectedResponse.currentPlayer, currentGame.currentPlayer)
        assertEquals(step.expectedResponse.pits, currentGame.board.pits)
        assertEquals(step.expectedResponse.gameState, currentGame.gameState)
        assertEquals(step.expectedResponse.version, currentGame.version)
        assertEquals(step.expectedResponse.winner, currentGame.winner)

        return currentGame
    }

    private fun loadScenario(scenario: String): URL =
        this::class.java.getResource("/games/$scenario.json")
            ?: throw IllegalStateException("Scenario $scenario not found")
}
