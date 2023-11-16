package com.bol.mancalaapp.rules

import com.bol.mancalaapp.domain.Board
import com.bol.mancalaapp.domain.Game
import com.bol.mancalaapp.domain.GameState
import com.bol.mancalaapp.domain.Player

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
     * Returns the number of regular pits for each player.
     */
    fun pitsForPlayer() = game.board.pitsForPlayer

    /**
     * Returns the list of all pits on the board.
     */
    fun pits() = game.board.pits

    /**
     * Returns the total number of pits on the board.
     */
    fun totalPits() = game.board.pits.size

    /**
     * Gets the number of stones in a specific pit.
     *
     * @param pitIdx The index of the pit.
     */
    fun getStonesInPit(pitIdx: Int) = game.board.pits[pitIdx]

    /**
     * Calculates the index of the pit opposite to the given pit.
     *
     * @param pitIdx The index of the pit.
     */
    fun getOppositePitIndex(pitIdx: Int) = Board.getOppositePitIndex(totalPits(), pitIdx)

    /**
     * Checks if a pit is owned by the current player.
     *
     * @param pitIdx The index of the pit.
     */
    fun isPitOwnedByCurrentPlayer(pitIdx: Int) = Board.isPitOwnedByCurrentPlayer(player(), pitIdx, totalPits())

    /**
     * Checks if a pit is not owned by the current player.
     *
     * @param pitIdx The index of the pit.
     */
    fun isPitNotOwnedByCurrentPlayer(pitIdx: Int) = isPitOwnedByCurrentPlayer(pitIdx).not()

    /**
     * Gets the index of the Mancala pit for the specified player.
     *
     * @param player The player whose Mancala pit index is to be retrieved. Defaults to the current player.
     */
    fun getPlayerMancalaIndex(player: Player = player()) = Board.getPlayerMancalaIndex(player, pitsForPlayer())

    /**
     * Returns the current game status.
     */
    fun gameState() = game.gameState

    // Convenience methods for updating the game context

    fun withPits(pits: List<Int>) = copy(game = game.copy(board = game.board.copy(pits = pits)))

    fun withPlayer(player: Player) = copy(game = game.copy(currentPlayer = player))

    fun withWinner(player: Player?) = copy(game = game.copy(winner = player))

    fun withGameState(gameState: GameState) = copy(game = game.copy(gameState = gameState))

    fun withLastPitIdx(lastPitIdx: Int) = copy(lastPitIdx = lastPitIdx)
}
