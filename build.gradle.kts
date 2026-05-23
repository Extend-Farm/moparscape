import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion

allprojects {
  group = "com.veyrmoor"
  version = "0.1.0-SNAPSHOT"
}

subprojects {
  repositories {
    mavenCentral()
  }

  pluginManager.withPlugin("java") {
    extensions.configure(JavaPluginExtension::class.java) {
      toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    dependencies.apply {
      add("testImplementation", platform("org.junit:junit-bom:5.10.2"))
      add("testImplementation", "org.junit.jupiter:junit-jupiter")
      add("testImplementation", "org.assertj:assertj-core:3.25.3")
    }

    tasks.withType(JavaCompile::class.java).configureEach {
      options.encoding = "UTF-8"
      options.release.set(21)
      options.compilerArgs.add("-parameters")
    }

    tasks.withType(Test::class.java).configureEach {
      useJUnitPlatform()
    }
  }
}
