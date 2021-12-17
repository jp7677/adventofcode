group = "me.jp7677"
version = "0.1-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "1.6.0"
    id("com.adarshr.test-logger") version "3.1.0"
}

repositories { mavenCentral() }
dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.6.0")
}

application { mainClass.set("MainKt") }
testlogger { theme = com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN }
tasks.jar { manifest { attributes["Main-Class"] = "MainKt" } }
tasks.compileKotlin { kotlinOptions.jvmTarget = "17" }
tasks.compileTestKotlin { kotlinOptions.jvmTarget = "17" }
tasks.test {
    useJUnitPlatform()
    testLogging { showStandardStreams = true }
}