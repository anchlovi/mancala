package com.bol.mancalaapp.domain.impl

import com.bol.mancalaapp.db.ApplicationShutDownConfiguration
import com.bol.mancalaapp.db.DBTestConfiguration
import com.bol.mancalaapp.domain.GamesRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@Import(DBTestConfiguration::class, ApplicationShutDownConfiguration::class)
class GamesRepositoryImplTest : GamesRepositoryContractTest() {
    @Autowired
    private lateinit var dslContext: DSLContext

    @Autowired
    private lateinit var mapper: ObjectMapper

    override fun createRepository(): GamesRepository = GamesRepositoryImpl(dslContext, mapper)
}