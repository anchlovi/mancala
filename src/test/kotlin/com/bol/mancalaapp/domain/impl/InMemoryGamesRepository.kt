package com.bol.mancalaapp.domain.impl

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameNotFoundException
import com.bol.mancalaapp.domain.GamesRepository
import com.bol.mancalaapp.domain.VersionMismatchException
import java.util.concurrent.ConcurrentHashMap

class InMemoryGamesRepository : GamesRepository {

    private val games = ConcurrentHashMap<GameId, Game>()

    override fun create(game: Game): GameId {
        games[game.id] = game
        return game.id
    }

    override fun findById(id: GameId): Game {
        return games[id] ?: throw GameNotFoundException(id)
    }

    override fun update(game: Game): Game {
        val existingGame = games[game.id]
            ?: throw GameNotFoundException(game.id)

        if (existingGame.version != game.version) {
            throw VersionMismatchException()
        }

        games[game.id] = game.copy(version = game.version + 1)
        return games[game.id]!!
    }

    override fun findByIdAndVersion(id: GameId, version: Int): Game {
        val game = games[id]
            ?: throw GameNotFoundException(id)

        if (game.version != version) {
            throw GameNotFoundException(id)
        }

        return game
    }
}
