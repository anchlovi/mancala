import kotlin.io.path.absolutePathString

repositories {
    mavenCentral()
}

plugins {
    val kotlinVersion = "1.9.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    id("nu.studer.jooq") version "8.2"
}

group = "com.bol"
version = "0.0.1-SNAPSHOT"

ext["jooq.version"] = jooq.version.get()

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.jooq:jooq")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.postgresql:postgresql")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.testcontainers:testcontainers-bom:1.18.3"))
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.0")


    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    jooqGenerator("org.postgresql:postgresql")
    jooqGenerator("org.jooq:jooq-meta-extensions")
}

jooq {
    configurations {
        create("main") {
            jooqConfiguration.apply {
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                        properties = listOf(
                            org.jooq.meta.jaxb.Property().apply {
                                key = "scripts"
                                value = sourceSets.main.map {
                                    it.resources.sourceDirectories.singleFile
                                        .toPath()
                                        .resolve("db/scripts/01.sql")
                                        .absolutePathString()
                                }.get()
                            },
                            org.jooq.meta.jaxb.Property().apply {
                                key = "defaultNameCase"
                                value = "lower"
                            }
                        )
                    }
                }
            }
        }
    }
}

tasks.named<nu.studer.gradle.jooq.JooqGenerate>("generateJooq") { allInputsDeclared.set(true) }

tasks.withType<Test> {
    useJUnitPlatform()
    jvmArgs("-Xshare:off")
}

tasks.bootBuildImage {
    builder.set("paketobuildpacks/builder-jammy-base:latest")
}
