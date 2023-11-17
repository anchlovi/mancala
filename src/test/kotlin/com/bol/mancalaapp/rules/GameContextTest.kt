package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.domain.Player
import com.bol.mancalaapp.helpers.GamesHelper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameContextTest {

    private lateinit var gameContext: GameContext
    private lateinit var game: Game
    private lateinit var board: Board

    @BeforeEach
    fun setUp() {
        gameContext = GamesHelper.newGameContext()
        game = gameContext.game
        board = game.board
    }

    @Test
    fun `should correctly return board`() {
        assertEquals(board, gameContext.board())
    }

    @Test
    fun `should correctly return player`() {
        assertEquals(game.currentPlayer, gameContext.player())
    }

    @Test
    fun `should correctly return isPitOwnedByCurrentPlayer`() {
        val pitIdx = 2

        assertEquals(board.isPitInRow(game.currentPlayer.ordinal, pitIdx), gameContext.isPitOwnedByCurrentPlayer(pitIdx))
    }

    @Test
    fun `should correctly return getPlayerMancalaIndex`() {
        assertEquals(board.getRowMancalaPit(game.currentPlayer.ordinal), gameContext.getPlayerMancalaIndex())
    }

    @Test
    fun `should correctly return gameState`() {
        assertEquals(game.gameState, gameContext.gameState())
    }

    @Test
    fun `should correctly return withPlayer`() {
        val newPlayer = game.currentPlayer.opponent

        // Assert
        assertEquals(newPlayer, gameContext.withPlayer(newPlayer).player())
    }

    @Test
    fun `should correctly return withWinner`() {
        val newWinner = Player.PLAYER1

        assertEquals(newWinner, gameContext.withWinner(newWinner).game.winner)
        assertNull(gameContext.withWinner(null).game.winner)
    }

    @Test
    fun `should correctly return withGameState`() {
        val newGameState = GameState.GAME_OVER

        assertEquals(newGameState, gameContext.withGameState(newGameState).gameState())
    }

    @Test
    fun `should correctly return withLastPitIdx`() {
        val newLastPitIdx = 4

        assertEquals(newLastPitIdx, gameContext.withLastPitIdx(newLastPitIdx).lastPitIdx)
    }
}