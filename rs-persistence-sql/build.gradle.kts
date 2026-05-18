plugins {
  `java-library`
}

import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the

dependencies {
  implementation(project(":rs-persistence-api"))
  implementation(libs.flyway.core)
  implementation(libs.flyway.database.postgresql)
  implementation(libs.postgres)
  implementation(libs.slf4j.api)
  runtimeOnly(libs.logback.classic)
  testImplementation(platform(libs.testcontainers.bom))
  testImplementation(libs.testcontainers.junit.jupiter)
  testImplementation(libs.testcontainers.postgresql)
}

val mainRuntimeClasspath = the<SourceSetContainer>()["main"].runtimeClasspath

tasks.register<JavaExec>("testDevDatabaseConnection") {
  group = "database"
  description = "Tests the configured development PostgreSQL connection."
  dependsOn(tasks.named("classes"))
  classpath = mainRuntimeClasspath
  mainClass.set("io.github.ffakira.rsps.persistence.sql.PostgresDevMain")
  args("test-connection")
}

tasks.register<JavaExec>("migrateDevDatabase") {
  group = "database"
  description = "Runs Flyway migrations against the configured development PostgreSQL database."
  dependsOn(tasks.named("classes"))
  classpath = mainRuntimeClasspath
  mainClass.set("io.github.ffakira.rsps.persistence.sql.PostgresDevMain")
  args("migrate")
}

tasks.register<JavaExec>("importCharacterToDevDatabase") {
  group = "database"
  description = "Imports one character file into the configured development PostgreSQL database."
  dependsOn(tasks.named("classes"))
  classpath = mainRuntimeClasspath
  mainClass.set("io.github.ffakira.rsps.persistence.sql.PostgresDevMain")
  doFirst {
    val characterName = project.findProperty("character") as String?
        ?: throw org.gradle.api.GradleException("Pass -Pcharacter=<username>")
    args("import-character", characterName)
  }
}
