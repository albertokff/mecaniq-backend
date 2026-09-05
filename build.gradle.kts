
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(ktorLibs.plugins.ktor)
    kotlin("plugin.serialization") version "2.0.21"
}

group = "com.mecaniq"
version = "1.0.0-SNAPSHOT"

application {
    mainClass = "com.mecaniq.MainKt"
}

kotlin {
    jvmToolchain(21)
}

val exposedVersion = "0.58.0" // Versão compatível com Ktor 3.x
val hikariVersion = "6.2.1"
val postgresVersion = "42.7.5" // Ou mysql-connector-j se for usar MySQL

dependencies {
    implementation(ktorLibs.server.callLogging)
    implementation(ktorLibs.server.contentNegotiation)
    implementation(ktorLibs.server.core)
    implementation(ktorLibs.server.cors)
    implementation(ktorLibs.server.netty)
    implementation(libs.logback.classic)

    implementation("org.jetbrains.exposed:exposed-core:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-dao:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:${exposedVersion}")
    implementation("com.zaxxer:HikariCP:${hikariVersion}")
    implementation("org.postgresql:postgresql:${postgresVersion}")
    implementation("com.h2database:h2:2.3.232")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")

    testImplementation(kotlin("test"))
    testImplementation(ktorLibs.server.testHost)
}
