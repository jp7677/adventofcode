import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "me.jp7677"
version = "0.1-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "1.7.22"
    id("com.adarshr.test-logger") version "3.2.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

repositories { mavenCentral() }
dependencies {
    testImplementation(kotlin("test"))
    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.kotest:kotest-assertions-core:5.5.4")
}

application { mainClass.set("MainKt") }
testlogger { theme = com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN }
tasks {
    withType<Jar> { manifest { attributes["Main-Class"] = "MainKt" } }
    withType<KotlinCompile>().all { kotlinOptions.jvmTarget = "17" }
    withType<Test> {
        useJUnitPlatform()
        testLogging { showStandardStreams = true }
    }
}
