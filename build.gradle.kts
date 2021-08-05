import java.net.URI

plugins {
    `java-library`
    id("io.freefair.lombok") version "6.0.0-m2"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

allprojects {
    group = "com.github.otymko.jos"
    version = "0.0.1"

    repositories {
        mavenCentral()
        maven { url = URI("https://jitpack.io") }
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")

    dependencies {
        // https://github.com/1c-syntax/bsl-parser
        api("com.github.1c-syntax", "bsl-parser", "0.19.3")

        implementation("commons-io", "commons-io", "2.8.0")
        implementation("org.apache.commons", "commons-lang3", "3.12.0")

        compileOnly("org.projectlombok:lombok:1.18.20")
        annotationProcessor("org.projectlombok:lombok:1.18.20")

        testCompileOnly("org.projectlombok:lombok:1.18.20")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.20")


        // Use JUnit Jupiter
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")

        testImplementation("com.ginsberg:junit5-system-exit:1.1.1")

        testImplementation("org.assertj:assertj-core:3.20.2")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    val test by tasks.getting(Test::class) {
        // Use junit platform for unit tests
        useJUnitPlatform()
    }

}
