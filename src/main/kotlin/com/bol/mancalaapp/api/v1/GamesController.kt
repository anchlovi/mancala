package com.bol.mancalaapp.api.v1

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.api.v1.requests.CreateNewGameRequest
import com.bol.mancalaapp.api.v1.responses.GameResponse
import com.bol.mancalaapp.usecases.create.CreateNewGameUseCase
import com.bol.mancalaapp.usecases.find.FindGameByIdUseCase
import com.bol.mancalaapp.usecases.play.PlayCommand
import com.bol.mancalaapp.usecases.play.PlayUseCase
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletionStage

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
@Validated
class GamesController(
    private val createUseCase: CreateNewGameUseCase,
    private val findGameByIdUseCase: FindGameByIdUseCase,
    private val playUseCase: PlayUseCase
) {
    /**
     * Endpoint for creating a new game.
     *
     * @param newGameRequest The request body containing the parameters for the new game.
     * @return A [CompletionStage] with [GameResponse] representing the newly created game.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody newGameRequest: CreateNewGameRequest): CompletionStage<GameResponse> =
        createUseCase.createNewGame(newGameRequest.toCommand()).thenApply { GameResponse.fromGame(it) }

    /**
     * Endpoint for retrieving a game by its ID.
     *
     * @param gameId The unique identifier of the game to be retrieved.
     * @return A [CompletionStage] with [GameResponse] representing the found game.
     */
    @GetMapping("/{gameId}")
    fun findById(@PathVariable gameId: GameId): CompletionStage<GameResponse> =
        findGameByIdUseCase.findGame(gameId).thenApply { GameResponse.fromGame(it) }

    /**
     * Endpoint for playing a move in a game.
     *
     * @param gameId The unique identifier of the game in which the move is played.
     * @param pitIdx The index of the pit from which the move is initiated.
     * @param version The version of the game for optimistic locking.
     * @return A [CompletionStage] with [GameResponse] representing the updated state of the game after the move.
     */
    @PutMapping("/{gameId}/play/{pitIdx}")
    fun play(@PathVariable gameId: GameId, @PathVariable pitIdx: Int, @RequestParam(required = true) version: Int): CompletionStage<GameResponse> =
        playUseCase.play(PlayCommand(gameId, pitIdx, version)).thenApply { GameResponse.fromGame(it) }
}