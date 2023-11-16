package com.bol.mancalaapp.domain

import com.bol.mancalaapp.GameId
import java.util.concurrent.CompletionStage

/**
 * Interface for the repository managing the persistence of game data in the Mancala game.
 *
 * This repository provides abstract methods for creating, finding, and updating game data,
 * abstracting the details of how the data is stored and retrieved.
 */
interface GamesRepository {
    /**
     * Persists a new game in the repository.
     *
     * @param game The game object to be created and persisted.
     * @return A [CompletionStage] that, when completed, provides the unique identifier of the newly created game.
     */
    fun create(game: Game): CompletionStage<GameId>

    /**
     * Retrieves a game by its unique identifier.
     *
     * @param id The unique identifier of the game to be retrieved.
     * @return A [CompletionStage] that, when completed, provides the requested game.
     * @throws GameNotFoundException If no game is found with the provided identifier.
     */
    fun findById(id: GameId): CompletionStage<Game>

    /**
     * Updates an existing game in the repository.
     *
     * @param game The game object with updated information to be persisted.
     * @return A [CompletionStage] that, when completed, provides the updated game.
     * @throws GameNotFoundException If no game is found with the provided game identifier.
     * @throws VersionMismatchException If there is a version conflict during the update.
     */
    fun update(game: Game): CompletionStage<Game>

    /**
     * Retrieves a game by its unique identifier and version number from the repository.
     *
     * This method is used to fetch a specific version of a game, ensuring that the game state matches
     * both the identifier and the version provided. It's particularly useful for operations that require
     * consistency checks or concurrency control.
     *
     * @param id The unique identifier of the game to be retrieved.
     * @param version The version number of the game to be matched.
     * @return A [CompletionStage] that, when completed, provides the requested game.
     * @throws GameNotFoundException If no game is found with the provided identifier and version.
     */
    fun findByIdAndVersion(id: GameId, version: Int): CompletionStage<Game>
}

