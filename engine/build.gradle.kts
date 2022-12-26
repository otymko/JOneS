description = "JOneScript Engine"

dependencies {
    implementation(project(":core"))

    // https://github.com/1c-syntax/bsl-parser
    api("com.github.1c-syntax", "bsl-parser", "0.21.0") {
        exclude("com.tunnelvisionlabs", "antlr4-annotations")
        exclude("com.ibm.icu", "*")
        exclude("org.antlr", "ST4")
        exclude("org.abego.treelayout", "org.abego.treelayout.core")
        exclude("org.antlr", "antlr-runtime")
        exclude("org.glassfish", "javax.json")
    }

    implementation("commons-io", "commons-io", "2.8.0")
    implementation("org.apache.commons", "commons-lang3", "3.12.0")
}