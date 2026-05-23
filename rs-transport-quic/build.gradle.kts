plugins {
  application
}

dependencies {
  implementation(project(":rs-client-core"))
  implementation(project(":rs-persistence-api"))
  implementation(project(":rs-persistence-sql"))
  implementation(project(":rs-protocol"))
  implementation(project(":rs-server-runtime"))
  implementation(libs.slf4j.api)
  runtimeOnly(libs.logback.classic)

  implementation(platform(libs.netty.bom))
  implementation(libs.netty.codec)
  implementation(libs.netty.codec.classes.quic)
  implementation(libs.netty.handler)
  implementation(libs.netty.transport)
  implementation(libs.bouncycastle.bcpkix)
  implementation(libs.bouncycastle.bcprov)
  implementation(libs.bouncycastle.bctls)
  runtimeOnly("${libs.netty.codec.native.quic.get()}:linux-x86_64")
}

application {
  mainClass.set("com.veyrmoor.transport.quic.QuicServerMain")
}
