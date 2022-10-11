import java.net.URI

plugins {
    `java-library`
    `maven-publish`
    jacoco
    id("io.freefair.lombok") version "6.5.1"
    id("net.kyori.indra.license-header") version "3.0.0"
    id("org.sonarqube") version "3.4.0.2513"
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
    apply(plugin = "net.kyori.indra.license-header")
    apply(plugin = "jacoco")
    apply(plugin = "maven-publish")

    dependencies {
        // Lombok
        compileOnly("org.projectlombok:lombok:1.18.24")
        annotationProcessor("org.projectlombok:lombok:1.18.24")
        testCompileOnly("org.projectlombok:lombok:1.18.20")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.20")

        // Use JUnit Jupiter
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")

        testImplementation("com.ginsberg:junit5-system-exit:1.1.2")
        testImplementation("org.assertj:assertj-core:3.23.1")
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

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
                pom {
                    description.set("Java OneScript Engine")
                    url.set("https://github.com/otymko/JOneS")
                    licenses {
                        license {
                            name.set("Mozilla Public License Version 2.0")
                            url.set("https://www.mozilla.org/en-US/MPL/2.0/")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("otymko")
                            name.set("Oleg Tymko")
                            email.set("olegtymko@yandex.ru")
                            url.set("https://github.com/otymko")
                        }
                        developer {
                            id.set("evilbeaver")
                            name.set("Andrey Ovsiankin")
                            email.set("ovsiankin.aa@gmail.com")
                            url.set("https://github.com/EvilBeaver")
                        }
                    }
                    scm {
                        connection.set("scm:git:https://github.com/otymko/JOneS.git")
                        developerConnection.set("scm:git:git@github.com:otymko/JOneS.git")
                        url.set("https://github.com/otymko/JOneS")
                    }
                }
            }
        }
    }
}
