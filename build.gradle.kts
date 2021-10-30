plugins {
    kotlin("jvm") version "1.5.31"
}

group = "de.moyapro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotest = "4.6.3"
val junit = "5.8.1"
val result = "5.2.0"
val kotlin = "1.5.31"
val coroutines = "1.5.2"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin")
    implementation("com.github.kittinunf.result:result-jvm:$result")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines")
    testImplementation("io.kotest:kotest-assertions-core-jvm:$kotest")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junit")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit")
}
