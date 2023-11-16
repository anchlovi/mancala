package com.bol.mancalaapp.domain.impl

import com.bol.mancalaapp.domain.GamesRepository

class InMemoryGamesRepositoryTest : GamesRepositoryContractTest() {
    override fun createRepository(): GamesRepository = InMemoryGamesRepository()
}