package com.bol.mancalaapp.usecases.find

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GamesRepository
import org.springframework.stereotype.Service

/**
 * Service class for retrieving game data based on a game identifier.
 * This class provides functionality to find a specific game by its unique ID.
 *
 * @property gamesRepository The repository responsible for managing game data retrieval.
 */
@Service
class FindGameByIdUseCase(
    private val gamesRepository: GamesRepository
) {
    /**
     * Retrieves a game from the repository based on its unique identifier.
     *
     * @param gameId The unique identifier of the game to retrieve.
     * @return The requested [Game].
     * @throws com.bol.mancalaapp.domain.GameNotFoundException If no game is found with the provided identifier.
     */
    fun findGame(gameId: GameId): Game = gamesRepository.findById(gameId)
}