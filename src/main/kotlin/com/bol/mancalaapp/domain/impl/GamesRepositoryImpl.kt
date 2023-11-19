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
    override fun create(game: Game): GameId {
        dslContext.insertInto(
            GAMES
        ).values(
            game.id,
            0,
            mapper.writeValueAsString(game)
        ).execute()

        return game.id
    }

    override fun findById(id: GameId): Game {
        val record = dslContext
            .selectFrom(GAMES)
            .where(GAMES.ID.eq(id))
            .fetchOne()

        return record?.let { mapRecordToGame(it) }
            ?: throw GameNotFoundException(id)
    }

    override fun update(game: Game): Game {
        val gameWithUpdatedVersion = incrementVersion(game)

        val updateCount = dslContext.update(GAMES)
            .set(GAMES.VERSION, gameWithUpdatedVersion.version)
            .set(GAMES.DATA, json(mapper.writeValueAsString(gameWithUpdatedVersion)))
            .where(GAMES.ID.eq(game.id))
            .and(GAMES.VERSION.eq(game.version))
            .execute()

        return when (updateCount) {
            1 -> gameWithUpdatedVersion
            else -> checkAndUpdateFailureReason(game)
        }
    }

    override fun findByIdAndVersion(id: GameId, version: Int): Game {
        val record = dslContext
            .selectFrom(GAMES)
            .where(GAMES.ID.eq(id))
            .and(GAMES.VERSION.eq(version))
            .fetchOne()

        return record?.let { mapRecordToGame(it) }
            ?: throw GameNotFoundException(id)
    }

    private fun checkAndUpdateFailureReason(game: Game):Game {
        findById(game.id)
        throw VersionMismatchException()
    }

    private fun mapRecordToGame(record: GamesRecord?): Game? =
        record?.let { mapper.readValue(it.data.data(), Game::class.java) }

    private fun incrementVersion(game: Game) = game.copy(version = game.version + 1)
}