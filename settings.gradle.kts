pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

rootProject.name = "moparscape"

include(":emulator")
project(":emulator").projectDir = file("client")

include(":game-client")
project(":game-client").projectDir = file("server/moparscape")
