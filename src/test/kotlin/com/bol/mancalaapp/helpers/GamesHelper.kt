package com.bol.mancalaapp.helpers

import com.bol.mancalaapp.GameId
import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.rules.GameContext

object GamesHelper {
    /**
     * Creates a new randomly initialized [Game] instance.
     *
     * @return A new [Game] instance with random values.
     */
    fun newGame(): Game {
        return Game(
            id = GameId.randomUUID(),
            board = newBoard(),
            totalPlayers = 2,
            currentPlayer = 0,
            gameState = GameState.entries.toTypedArray().random(),
            winner = if (Math.random() > 0.5) 0 else null,
            version = 0
        )
    }

    /**
     * Creates a new randomly initialized [Board] instance.
     *
     * @return A new [Board] instance with random pits and stones.
     */
    fun newBoard(): Board {
        val pitsForPlayer = (4..6).random() // Random number of pits for each player
        val totalPits = pitsForPlayer * 2 + 2 // Total pits including Mancalas
        val pits = List(totalPits) { index ->
            if (index == pitsForPlayer || index == totalPits - 1) 0 // Mancalas start empty
            else (1..6).random() // Random number of stones in each pit
        }

        return Board(
            pits = pits,
            pitsPerRow = pitsForPlayer
        )
    }

    /**
     * Creates a new randomly initialized [GameContext] instance.
     *
     * @return A new [GameContext] instance with random pits and stones.
     */
    fun newGameContext(): GameContext =
        GameContext(
            game = newGame(),
            pitIdx = (0..13).random()
        )
}
