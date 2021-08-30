import java.net.URI

plugins {
    `java-library`
    jacoco
    id("io.freefair.lombok") version "6.0.0-m2"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("net.kyori.indra.license-header") version "2.0.6"
    id("org.sonarqube") version "3.3"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

allprojects {
    group = "com.github.otymko.jos"
    version = "0.0.1"

    repositories {
        mavenCentral()
        maven { url = URI("https://jitpack.io") }
    }

    sonarqube {
        properties {
            property("sonar.sourceEncoding", "UTF-8")
            property("sonar.host.url", "https://sonarcloud.io")
            property("sonar.organization", "otymko")
            property("sonar.projectKey", "otymko_JOneS")
            property("sonar.projectName", "JOneS")
            property("sonar.exclusions", "**/gen/**/*.*")
            property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacoco.xml")
        }
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")
    apply(plugin = "net.kyori.indra.license-header")
    apply(plugin = "jacoco")

    dependencies {
        // https://github.com/1c-syntax/bsl-parser
        api("com.github.1c-syntax", "bsl-parser", "0.19.3")

        implementation("commons-io", "commons-io", "2.8.0")
        implementation("org.apache.commons", "commons-lang3", "3.12.0")

        compileOnly("org.projectlombok:lombok:1.18.20")
        annotationProcessor("org.projectlombok:lombok:1.18.20")

        testCompileOnly("org.projectlombok:lombok:1.18.20")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.20")

        implementation("org.reflections:reflections:0.9.11")

        // Use JUnit Jupiter
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")

        testImplementation("com.ginsberg:junit5-system-exit:1.1.1")

        testImplementation("org.assertj:assertj-core:3.20.2")
    }

    val test by tasks.getting(Test::class) {
        // Use junit platform for unit tests
        useJUnitPlatform()
    }

    tasks.check {
        dependsOn(tasks.jacocoTestReport)
    }

    tasks.jacocoTestReport {
        reports {
            xml.isEnabled = true
            xml.destination = File("$buildDir/reports/jacoco/test/jacoco.xml")
        }
    }

    license {
        header(rootProject.file("license_header.txt"))
        exclude("**/*.properties")
        exclude("**/*.xml")
        exclude("**/*.json")
        exclude("**/*.txt")
        exclude("**/*.java.orig")
        exclude("**/*.impl")
    }

}
