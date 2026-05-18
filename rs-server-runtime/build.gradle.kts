plugins {
  application
}

dependencies {
  implementation(project(":rs-content"))
  implementation(project(":rs-persistence-api"))
  implementation(project(":rs-protocol"))
  implementation(project(":rs-sim-ecs"))
  implementation(libs.slf4j.api)
  runtimeOnly(libs.logback.classic)
}

application {
  mainClass.set("io.github.ffakira.rsps.server.runtime.ServerRuntimeMain")
}
