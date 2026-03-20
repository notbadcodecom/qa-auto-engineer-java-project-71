plugins {
    id("java")
    id("application")
    id("checkstyle")
    id("jacoco")
    id("com.github.ben-manes.versions") version "0.53.0"
    id("org.sonarqube") version "7.2.3.7755"
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

val picocliVersion = "4.7.7"
val jacksonVersion = "2.21.1"
val junitBomVersion = "6.0.2"
val assertjVersion = "3.27.7"

repositories {
    mavenCentral()
}

dependencies {
    implementation("info.picocli:picocli:$picocliVersion")
    implementation("info.picocli:picocli-codegen:$picocliVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:$jacksonVersion}")

    testImplementation(platform("org.junit:junit-bom:$junitBomVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sonar {
    properties {
        property("sonar.projectKey", "notbadcodecom_qa-auto-engineer-java-project-71")
        property("sonar.organization", "notbadcode")
        property("sonar.projectName", "qa-auto-engineer-java-project-71")
    }
}

application {
    mainClass = "hexlet.code.App"
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}
