plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
    java
}

repositories {
    mavenCentral()
    mavenLocal()
    maven {
      url = uri("https://repo.akka.io/maven")
    }

}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation(libs.guava)
    implementation("io.vertx:vertx-core:4.5.7")
    implementation("io.vertx:vertx-web:4.5.7")
    implementation("io.vertx:vertx-web-client:4.5.7")
    implementation("org.jsoup:jsoup:1.14.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")

    /* module 2.2 -- RxJava */
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")

    // Akka
    implementation("com.typesafe.akka:akka-actor-typed_2.13:2.9.3")
    implementation ("ch.qos.logback:logback-classic:1.2.13")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "simtrafficexamples.RunMassiveTrafficSimulation"
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests
    useJUnitPlatform()
}
