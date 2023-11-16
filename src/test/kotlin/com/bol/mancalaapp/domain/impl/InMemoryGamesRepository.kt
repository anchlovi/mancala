package com.bol.mancalaapp.domain.impl

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameNotFoundException
import com.bol.mancalaapp.domain.GamesRepository
import com.bol.mancalaapp.domain.VersionMismatchException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage
import java.util.concurrent.ConcurrentHashMap

class InMemoryGamesRepository : GamesRepository {

    private val games = ConcurrentHashMap<GameId, Game>()

    override fun create(game: Game): CompletionStage<GameId> {
        games[game.id] = game
        return CompletableFuture.completedFuture(game.id)
    }

    override fun findById(id: GameId): CompletionStage<Game> {
        val game = games[id] ?: throw GameNotFoundException(id)
        return CompletableFuture.completedFuture(game)
    }

    override fun update(game: Game): CompletionStage<Game> {
        val existingGame = games[game.id]
            ?: throw GameNotFoundException(game.id)

        if (existingGame.version != game.version) {
            throw VersionMismatchException()
        }

        games[game.id] = game.copy(version = game.version + 1)
        return CompletableFuture.completedFuture(games[game.id])
    }

    override fun findByIdAndVersion(id: GameId, version: Int): CompletionStage<Game> {
        val game = games[id]
            ?: throw GameNotFoundException(id)

        if (game.version != version) {
            throw GameNotFoundException(id)
        }

        return CompletableFuture.completedFuture(game)
    }
}
