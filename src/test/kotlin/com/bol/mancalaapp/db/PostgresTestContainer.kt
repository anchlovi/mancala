package com.bol.mancalaapp.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.sql.Connection
import java.util.stream.Collectors
import javax.sql.DataSource

class PostgresTestContainer(imageName: String) :
    PostgreSQLContainer<PostgresTestContainer>(imageName) {

    private lateinit var _dataSource: HikariDataSource

    init {
        withDatabaseName(DB_NAME)
        withUsername(USER_NAME)
        withPassword(PASSWORD)
    }

    override fun start() {
        if (!isRunning) {
            super.start()
            waitingFor(
                Wait.forLogMessage(".*system is ready to accept connections*", 1)
            )
            initializeDataSource()
            migrate()
        }
    }

    private fun initializeDataSource() {
        val config =
            HikariConfig()
                .apply {
                    jdbcUrl = this@PostgresTestContainer.jdbcUrl
                    username = this@PostgresTestContainer.username
                    password = this@PostgresTestContainer.password
                    driverClassName = this@PostgresTestContainer.driverClassName
                }
        _dataSource = HikariDataSource(config)
    }

    private fun migrate() {
        val connection = _dataSource.connection
        connection.use { conn ->
            executeScriptsInOrder(conn, "/db/scripts")
        }
    }

    private fun executeScriptsInOrder(connection: Connection, resourceDirectory: String) {
        val resourceNames = getResourceFiles(resourceDirectory)
        resourceNames.sorted().forEach { resourceName ->
            val sql = readResourceAsString("$resourceDirectory/$resourceName")
            sql.split(";").forEach { statement ->
                if (statement.isNotBlank()) {
                    connection.createStatement().use {
                        it.execute(statement.trim())
                    }
                }
            }
        }
    }

    private fun getResourceFiles(path: String): List<String> {
        val filenames = mutableListOf<String>()
        val resourceAsStream = javaClass.getResourceAsStream(path)
        BufferedReader(InputStreamReader(resourceAsStream!!, StandardCharsets.UTF_8)).use { reader ->
            var filename = reader.readLine()
            while (filename != null) {
                filenames.add(filename)
                filename = reader.readLine()
            }
        }
        return filenames
    }

    private fun readResourceAsString(resource: String): String {
        return javaClass.getResourceAsStream(resource).use { inputStream ->
            inputStream!!.bufferedReader(StandardCharsets.UTF_8).lines().collect(Collectors.joining("\n"))
        }
    }

    override fun stop() {
        _dataSource.close()
        super.stop()
    }

    val dataSource: DataSource
        get() {
            if (!isRunning) {
                start()
            }
            return _dataSource
        }

    companion object {
        const val DB_NAME = "test-db"
        const val USER_NAME = "postgres"
        const val PASSWORD = "postgres"
    }
}
