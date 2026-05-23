plugins {
  `java-library`
  application
}

import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.the

application {
  mainClass.set("com.veyrmoor.persistence.sql.PostgresDevMain")
}

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
  mainClass.set("com.veyrmoor.persistence.sql.PostgresDevMain")
  args("test-connection")
}

tasks.register<JavaExec>("migrateDevDatabase") {
  group = "database"
  description = "Runs Flyway migrations against the configured development PostgreSQL database."
  dependsOn(tasks.named("classes"))
  classpath = mainRuntimeClasspath
  mainClass.set("com.veyrmoor.persistence.sql.PostgresDevMain")
  args("migrate")
}

tasks.register<JavaExec>("provisionDevAccount") {
  group = "database"
  description = "Provisions the configured development PostgreSQL account and starter character from environment variables."
  dependsOn(tasks.named("classes"))
  classpath = mainRuntimeClasspath
  mainClass.set("com.veyrmoor.persistence.sql.PostgresDevMain")
  args("provision-account")
}

tasks.register<JavaExec>("migrateAndProvisionDevDatabase") {
  group = "database"
  description = "Runs Flyway migrations and provisions the configured development PostgreSQL account and starter character."
  dependsOn(tasks.named("classes"))
  classpath = mainRuntimeClasspath
  mainClass.set("com.veyrmoor.persistence.sql.PostgresDevMain")
  args("migrate-and-provision")
}

tasks.register<JavaExec>("resetDevDatabase") {
  group = "database"
  description = "Wipes and remigrates the configured development PostgreSQL database."
  dependsOn(tasks.named("classes"))
  classpath = mainRuntimeClasspath
  mainClass.set("com.veyrmoor.persistence.sql.PostgresDevMain")
  args("reset")
}

tasks.register<JavaExec>("resetAndProvisionDevDatabase") {
  group = "database"
  description = "Wipes, remigrates, and provisions the configured development PostgreSQL account and starter character."
  dependsOn(tasks.named("classes"))
  classpath = mainRuntimeClasspath
  mainClass.set("com.veyrmoor.persistence.sql.PostgresDevMain")
  args("reset-and-provision")
}
