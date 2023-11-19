package com.bol.mancalaapp.rules

object EndGameTestDataProvider {

    val pitsForPlayer = (4..6).random()

    val player1MancalaStones = (1 until 30).random()
    val player2MancalaStones = (1 until 30).random()

    val pitsWithStones = List(pitsForPlayer) { (1 until 12).random() }
    val pitsWithoutStones = List(pitsForPlayer) { 0 }

    val player1WithoutStonesPits = pitsWithoutStones + player1MancalaStones + pitsWithStones + player2MancalaStones
    val player2WithoutStonesPits = pitsWithStones + player1MancalaStones + pitsWithoutStones + player2MancalaStones

    val gameInProgressPits = pitsWithStones + player1MancalaStones + pitsWithStones + player2MancalaStones

    fun pitsProvider(): List<List<Int>> {
        return listOf(player1WithoutStonesPits, player2WithoutStonesPits)
    }
}