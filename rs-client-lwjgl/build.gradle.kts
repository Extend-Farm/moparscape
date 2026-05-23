plugins {
  application
}

val lwjglVersion = libs.versions.lwjgl.get()

dependencies {
  implementation(project(":rs-client-core"))
  implementation(project(":rs-cache"))
  implementation(project(":rs-content"))
  implementation(project(":rs-persistence-api"))
  implementation(project(":rs-persistence-sql"))
  implementation(project(":rs-protocol"))
  implementation(project(":rs-server-runtime"))
  implementation(project(":rs-transport-quic"))
  implementation(libs.slf4j.api)
  runtimeOnly(libs.logback.classic)

  implementation(platform(libs.lwjgl.bom))
  implementation(libs.lwjgl)
  implementation(libs.lwjgl.glfw)
  implementation(libs.lwjgl.opengl)
  implementation(libs.lwjgl.stb)
  runtimeOnly("org.lwjgl:lwjgl:$lwjglVersion:natives-linux")
  runtimeOnly("org.lwjgl:lwjgl-glfw:$lwjglVersion:natives-linux")
  runtimeOnly("org.lwjgl:lwjgl-opengl:$lwjglVersion:natives-linux")
  runtimeOnly("org.lwjgl:lwjgl-stb:$lwjglVersion:natives-linux")

  testRuntimeOnly(project(":game-client"))
}

application {
  mainClass.set("com.veyrmoor.client.desktop.app.DesktopClientMain")
}
