package com.bol.mancalaapp.api.v1

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.api.v1.requests.CreateNewGameRequest
import com.bol.mancalaapp.api.v1.requests.PlayRequest
import com.bol.mancalaapp.api.v1.responses.GameResponse
import com.bol.mancalaapp.usecases.create.CreateNewGameCommand
import com.bol.mancalaapp.usecases.create.CreateNewGameUseCase
import com.bol.mancalaapp.usecases.find.FindGameByIdUseCase
import com.bol.mancalaapp.usecases.play.PlayUseCase
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * REST controller for managing game-related actions in the Mancala game application.
 * This controller provides endpoints for creating a new game, finding a game by its ID, and playing a move in a game.
 *
 * @property createUseCase The use case for creating a new game.
 * @property findGameByIdUseCase The use case for finding a game by its ID.
 * @property playUseCase The use case for playing a move in a game.
 */
@RestController
@RequestMapping("/api/v1/games")
class GamesController(
    private val createUseCase: CreateNewGameUseCase,
    private val findGameByIdUseCase: FindGameByIdUseCase,
    private val playUseCase: PlayUseCase
) {
    /**
     * Endpoint for creating a new game. It allows for optional customization of the game settings.
     * If no custom settings are provided, default settings are used.
     *
     * @param newGameRequest The request body containing the optional parameters for the new game.
     *                       If null, default game settings are used.
     * @return A [GameResponse] representing the newly created game.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create New Game")
    fun create(@RequestBody(required = false) newGameRequest: CreateNewGameRequest?): GameResponse {
        val command = newGameRequest?.toCommand() ?: CreateNewGameCommand.DefaultCommand
        val game = createUseCase.createNewGame(command)
        return GameResponse.fromGame(game)
    }

    /**
     * Endpoint for retrieving a game by its ID.
     *
     * @param gameId The unique identifier of the game to be retrieved.
     * @return A [GameResponse] representing the found game.
     */
    @GetMapping("/{gameId}")
    @Operation(summary = "Find Game By Id")
    fun findById(@PathVariable gameId: GameId): GameResponse =
        GameResponse.fromGame(findGameByIdUseCase.findGame(gameId))

    /**
     * Endpoint for playing a move in a game.
     *
     * @param gameId The unique identifier of the game to be updated.
     * @param playRequest The request body containing the parameters for the move.
     * @return A [GameResponse] representing the updated state of the game after the move.
     */
    @PutMapping("/{gameId}/play")
    @Operation(summary = "Play Move")
    fun play(@RequestBody playRequest: PlayRequest, @PathVariable gameId: GameId): GameResponse =
        GameResponse.fromGame(playUseCase.play(playRequest.toCommand(gameId)))
}