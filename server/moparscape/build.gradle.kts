plugins {
  java
  application
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
  }
}

sourceSets {
  main {
    java.setSrcDirs(listOf("."))
  }
}

application {
  mainClass.set("io.github.ffakira.moparscape.client.GameClient")
}

tasks.named<JavaExec>("run") {
  workingDir = layout.projectDirectory.asFile
}

tasks.named<Jar>("jar") {
  manifest {
    attributes["Main-Class"] = "io.github.ffakira.moparscape.client.GameClient"
  }
}
