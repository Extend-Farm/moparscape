pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

rootProject.name = "moparscape"

include(":emulator")
project(":emulator").projectDir = file("moparscape-reference/client")

include(":game-client")
project(":game-client").projectDir = file("moparscape-reference/server/moparscape")

include(":rs-model")
include(":rs-cache")
include(":rs-content")
include(":rs-sim-ecs")
include(":rs-persistence-api")
include(":rs-persistence-sql")
include(":rs-protocol")
include(":rs-server-runtime")
include(":rs-client-core")
include(":rs-client-lwjgl")
include(":rs-transport-quic")
include(":rs-test-fixtures")
