group = "me.jp7677"
version = "0.1-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "2.3.20"
    id("com.adarshr.test-logger") version "4.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
}

repositories { mavenCentral() }
dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:6.1.9")
    testImplementation("io.kotest:kotest-assertions-core:6.1.9")
}

testlogger { theme = com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN }

kotlin { jvmToolchain(25) }
application { mainClass = "MainKt" }
tasks {
    jar { manifest { attributes["Main-Class"] = "MainKt" } }
    test {
        useJUnitPlatform()
        testLogging { showStandardStreams = true }
    }
}
