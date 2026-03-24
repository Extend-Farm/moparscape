subprojects {
  repositories {
    mavenCentral()
  }

  tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
  }
}
