plugins {
    id("java") // Apply the Java plugin to the parent project
}

group = "org.example"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral() // Define the repository for all projects
    }
}

subprojects {
    apply(plugin = "java") // Apply the Java plugin to all submodules

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.36")
        annotationProcessor("org.projectlombok:lombok:1.18.36")
        implementation("org.json:json:20241224")
        implementation("org.apache.kafka:kafka-clients:3.9.0")
        implementation("org.slf4j:slf4j-api:2.0.16")
        implementation("org.slf4j:slf4j-simple:2.0.16")
    }
}
