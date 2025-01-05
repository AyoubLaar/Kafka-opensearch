plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.opensearch.client:opensearch-java:2.8.1")
    implementation("org.opensearch.client:opensearch-rest-high-level-client:2.18.0")

}

tasks.test {
    useJUnitPlatform()
}