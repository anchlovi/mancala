package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameState

data class GameContext(
    val game: Game,
    val pitIdx: Int,
    val lastPitIdx: Int = -1,
) {
    /**
     * Returns the current game board.
     */
    fun board() = game.board

    /**
     * Returns the current player.
     */
    fun player() = game.currentPlayer


    /**
     * Gets the index of the Mancala pit for the specified player.
     *
     * @param player The player whose Mancala pit index is to be retrieved. Defaults to the current player.
     */
    fun getPlayerMancalaIndex(player: Int = player()) = board().getRowMancalaPit(player)

    /**
     * Checks if a pit is owned by the current player.
     *
     * @param pitIdx The index of the pit.
     */
    fun isPitOwnedByCurrentPlayer(pitIdx: Int) = board().isPitInRow(player(), pitIdx)

    /**
     * Returns the current game status.
     */
    fun gameState() = game.gameState

    // Convenience methods for updating the game context

    fun withBoard(board: Board) = copy(game = game.copy(board = board))

    fun withPlayer(player: Int) = copy(game = game.copy(currentPlayer = player))

    fun withWinner(player: Int?) = copy(game = game.copy(winner = player))

    fun withGameState(gameState: GameState) = copy(game = game.copy(gameState = gameState))

    fun withLastPitIdx(lastPitIdx: Int) = copy(lastPitIdx = lastPitIdx)
}
