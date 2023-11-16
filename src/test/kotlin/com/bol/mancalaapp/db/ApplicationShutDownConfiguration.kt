package com.bol.mancalaapp.db

import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration

@TestConfiguration
class ApplicationShutDownConfiguration {

    @Autowired
    private lateinit var testContainer: PostgresTestContainer

    @PreDestroy
    fun onShutDown() {
        testContainer.stop()
    }
}
