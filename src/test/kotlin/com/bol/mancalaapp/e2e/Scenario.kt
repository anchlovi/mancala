package com.bol.mancalaapp.e2e

data class GameSetup(
    val totalPlayers: Int,
    val pitsPerPlayer: Int,
    val stonesPerPit: Int
)

data class InitialState(
    val version: Int,
    val pits: List<Int>,
    val currentPlayer: Int
)

data class ExpectedResponse(
    val currentPlayer: Int,
    val pits: List<Int>,
    val gameState: String,
    val version: Int,
    val winner: Int?
)

data class GameStep(
    val pitIndex: Int,
    val expectedResponse: ExpectedResponse
)

data class Scenario(
    val gameSetup: GameSetup,
    val initialState: InitialState,
    val gameSteps: List<GameStep>
)