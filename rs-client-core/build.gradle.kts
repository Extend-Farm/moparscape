plugins {
  `java-library`
}

dependencies {
  api(project(":rs-model"))
  implementation(project(":rs-content"))
  implementation(project(":rs-protocol"))
  testImplementation(project(":rs-persistence-api"))
  testImplementation(project(":rs-server-runtime"))
}
