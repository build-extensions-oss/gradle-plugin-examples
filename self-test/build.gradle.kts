plugins {
    kotlin("jvm") version embeddedKotlinVersion
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit)
    testRuntimeOnly(libs.junitPlatform)
}

tasks.test {
    useJUnitPlatform()
    systemProperty("examples.repository.root", rootDir.parentFile.absolutePath)
    systemProperty("self-test.project.root", rootDir.absolutePath)
}
