package com.bol.mancalaapp.rules.validators

import com.bol.mancalaapp.GameId

open class ValidationException(message: String) : RuntimeException(message)

class PitIsMancalaException(pitIdx: Int) : ValidationException("Pit [$pitIdx] is a mancala")

class InvalidPitException(pitIdx: Int) : ValidationException("Invalid pit [$pitIdx]")

class PitHasNoStonesException(pitIdx: Int) : ValidationException("Pit [$pitIdx] has no stones")

class PitDoesNotBelongToPlayerException(pitIdx: Int, player: Int) : ValidationException("Pit [$pitIdx] does not belong to player [$player]]")

class GameIsOverException(boardId: GameId) : ValidationException("Game [$boardId] is over")
