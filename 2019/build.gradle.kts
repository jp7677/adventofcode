group = "me.jp7677"
version = "0.1-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "1.5.31"
    id("com.adarshr.test-logger") version "3.0.0"
}

repositories { mavenCentral() }
dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.31")
}

application { mainClass.set("MainKt") }
testlogger { theme = com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN }
tasks.jar { manifest { attributes["Main-Class"] = "MainKt" } }
tasks.compileKotlin { kotlinOptions.jvmTarget = "11" }
tasks.compileTestKotlin { kotlinOptions.jvmTarget = "11" }
tasks.test { useJUnitPlatform() }