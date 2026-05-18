plugins {
  `java-library`
}

dependencies {
  api(project(":rs-model"))
  implementation(libs.commons.compress)
}
