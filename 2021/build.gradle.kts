group = "me.jp7677"
version = "0.1-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "1.7.21"
    id("com.adarshr.test-logger") version "3.2.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

repositories { mavenCentral() }
dependencies {
    testImplementation(kotlin("test"))
}

application { mainClass.set("MainKt") }
testlogger { theme = com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN }
tasks {
    jar { manifest { attributes["Main-Class"] = "MainKt" } }
    compileKotlin { kotlinOptions.jvmTarget = "17" }
    compileTestKotlin { kotlinOptions.jvmTarget = "17" }
    test {
        useJUnitPlatform()
        testLogging { showStandardStreams = true }
    }
}
