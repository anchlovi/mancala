package com.bol.mancalaapp.db

import org.jooq.SQLDialect
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class DBTestConfiguration {
    @Bean
    fun testContainer() = PostgresTestContainer(POSTGRES_IMAGE)

    @Bean
    fun dslContext(testContainer: PostgresTestContainer) =
        DefaultDSLContext(DefaultConfiguration().set(testContainer.dataSource).set(SQLDialect.POSTGRES))

    companion object {
        const val POSTGRES_IMAGE = "postgres:15-alpine"
    }
}
