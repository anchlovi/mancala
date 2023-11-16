package com.bol.mancalaapp

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * The main application class for the Mancala game Spring Boot application.
 * This class serves as the entry point for the Spring Boot application, enabling autoconfiguration and component scanning.
 *
 * The `@SpringBootApplication` annotation encompasses @Configuration, @EnableAutoConfiguration, and @ComponentScan annotations with their default attributes.
 */
@SpringBootApplication
class Application

/**
 * The main function that launches the Spring Boot application.
 *
 * @param args The command-line arguments passed to the application.
 */
fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
