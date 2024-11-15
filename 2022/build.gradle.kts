import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "me.jp7677"
version = "0.1-SNAPSHOT"

plugins {
    application
    kotlin("jvm") version "2.0.21"
    id("com.adarshr.test-logger") version "4.0.0"
    id("org.jmailen.kotlinter") version "4.4.1"
}

repositories { mavenCentral() }
dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
}

application { mainClass.set("MainKt") }
testlogger { theme = com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN }
tasks {
    withType<Jar> { manifest { attributes["Main-Class"] = "MainKt" } }
    withType<KotlinCompile>().configureEach { compilerOptions { jvmTarget = JvmTarget.JVM_21 } }
    withType<Test>().configureEach {
        useJUnitPlatform()
        testLogging { showStandardStreams = true }
    }
}
