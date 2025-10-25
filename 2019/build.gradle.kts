group = "me.jp7677"
version = "0.1-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "2.1.21"
    id("com.adarshr.test-logger") version "4.0.0"
    id("org.jlleitschuh.gradle.ktlint") version "13.1.0"
}

repositories { mavenCentral() }
dependencies {
    testImplementation(kotlin("test"))
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
