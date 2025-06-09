plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.5.1-SNAPSHOT"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.kahoot"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
}


tasks.withType<Test> {
    useJUnitPlatform()
}
