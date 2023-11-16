package com.bol.mancalaapp.domain.impl

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameNotFoundException
import com.bol.mancalaapp.domain.GamesRepository
import com.bol.mancalaapp.domain.VersionMismatchException
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.DSLContext
import org.jooq.JSON.json
import org.jooq.generated.tables.Games.GAMES
import org.jooq.generated.tables.records.GamesRecord
import org.springframework.stereotype.Repository
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

/**
 * A PostgreSQL implementation of the GamesRepository for managing game data in a relational database.
 *
 * This repository implementation uses JOOQ for database interactions and an ObjectMapper for JSON serialization and deserialization.
 *
 * @property dslContext The JOOQ DSLContext for database interaction.
 * @property mapper The ObjectMapper for converting Game objects to and from JSON.
 */
@Repository
class GamesRepositoryImpl(
    private val dslContext: DSLContext,
    private val mapper: ObjectMapper
): GamesRepository {
    override fun create(game: Game): CompletionStage<GameId> {
        val fut = dslContext.insertInto(
            GAMES
        ).values(
            game.id,
            0,
            mapper.writeValueAsString(game)
        ).executeAsync()

        return fut.thenApply {
            game.id
        }
    }

    override fun findById(id: GameId): CompletionStage<Game> {
        val records = dslContext
            .selectFrom(GAMES)
            .where(GAMES.ID.eq(id))
            .limit(1).fetchAsync()

        return records.thenApply {
            when {
                it.isNotEmpty -> mapRecordToGame(it[0])
                else -> throw GameNotFoundException(id)
            }
        }
    }

    override fun update(game: Game): CompletionStage<Game> {
        val gameWithUpdatedVersion = incrementVersion(game)

        return dslContext.update(GAMES)
            .set(GAMES.VERSION, gameWithUpdatedVersion.version)
            .set(GAMES.DATA, json(mapper.writeValueAsString(gameWithUpdatedVersion)))
            .where(GAMES.ID.eq(game.id))
            .and(GAMES.VERSION.eq(game.version))
            .executeAsync()
            .thenCompose { updateCount ->
                when (updateCount) {
                    1 -> CompletableFuture.completedFuture(gameWithUpdatedVersion)
                    else -> checkAndUpdateFailureReason(game)
                }
            }
    }

    override fun findByIdAndVersion(id: GameId, version: Int): CompletionStage<Game> {
        val records = dslContext
            .selectFrom(GAMES)
            .where(GAMES.ID.eq(id))
            .and(GAMES.VERSION.eq(version))
            .limit(1).fetchAsync()

        return records.thenApply {
            when {
                it.isNotEmpty -> mapRecordToGame(it[0])
                else -> throw GameNotFoundException(id)
            }
        }
    }

    private fun checkAndUpdateFailureReason(game: Game): CompletionStage<Game> {
        return findById(game.id).thenCompose {
            CompletableFuture.failedFuture(VersionMismatchException())
        }
    }

    private fun mapRecordToGame(record: GamesRecord?): Game? =
        record?.let { mapper.readValue(it.data.data(), Game::class.java) }

    private fun incrementVersion(game: Game) = game.copy(version = game.version + 1)
}