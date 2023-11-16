package com.bol.mancalaapp.domain

import com.bol.mancalaapp.GameId

class GameNotFoundException(gameId: GameId) : RuntimeException("Game with id [$gameId] not found")

class VersionMismatchException : RuntimeException("Version mismatch")