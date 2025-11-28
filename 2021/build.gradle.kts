import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "me.jp7677"
version = "0.1-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "2.2.21"
    id("com.adarshr.test-logger") version "4.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
}

repositories { mavenCentral() }
dependencies {
    testImplementation(kotlin("test"))
}

application { mainClass.set("MainKt") }
testlogger { theme = com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN }
tasks {
    jar { manifest { attributes["Main-Class"] = "MainKt" } }
    compileKotlin { compilerOptions.jvmTarget.set(JvmTarget.JVM_21) }
    compileTestKotlin { compilerOptions.jvmTarget.set(JvmTarget.JVM_21) }
    test {
        useJUnitPlatform()
        testLogging { showStandardStreams = true }
    }
}
