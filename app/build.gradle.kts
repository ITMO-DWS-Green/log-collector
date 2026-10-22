plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":ingestion"))
    implementation(project(":storage"))
    implementation(project(":detection-rules"))
    implementation(project(":detection-ml"))
    implementation(project(":incidents"))
    implementation(project(":evaluation"))
    implementation(project(":response"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
