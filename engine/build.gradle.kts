description = "JOneScript Engine"

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.github.otymko.jos.ScriptApplication"
        attributes["Implementation-Version"] = archiveVersion.get()
    }
    enabled = false
    dependsOn(tasks.shadowJar)
}

tasks.shadowJar {
    project.configurations.implementation.get().isCanBeResolved = true
    configurations = listOf(project.configurations["implementation"])
    archiveClassifier.set("")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-Xlint:unchecked")
    options.compilerArgs.add("-Xlint:deprecation")
}