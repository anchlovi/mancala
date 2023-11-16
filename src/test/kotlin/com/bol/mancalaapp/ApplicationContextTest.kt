package com.bol.mancalaapp

import com.bol.mancalaapp.configuration.DefaultTestConfiguration
import com.bol.mancalaapp.db.ApplicationShutDownConfiguration
import com.bol.mancalaapp.db.DBTestConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
@Import(DBTestConfiguration::class, ApplicationShutDownConfiguration::class)
@ContextConfiguration(classes = [DefaultTestConfiguration::class])
internal class ApplicationContextTest {

    @Test
    internal fun `Application context is correctly set up`() {
    }
}
