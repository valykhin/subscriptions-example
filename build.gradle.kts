import org.springframework.boot.gradle.tasks.bundling.BootJar
import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.bmuschko.docker-remote-api") version "9.4.0"
}

group = "ru.ivalykhin"
version = "0.0.1-SNAPSHOT"
description = "Subscription application"

val dockerImageName = "ghcr.io/valykhin/${rootProject.name}:${version}"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")

	implementation("org.mapstruct:mapstruct:1.6.3")
	implementation("io.micrometer:micrometer-registry-prometheus:1.15.0")
	implementation("org.liquibase:liquibase-core:4.31.1")
	implementation("io.hypersistence:hypersistence-utils-hibernate-63:3.9.9")

	compileOnly("org.projectlombok:lombok")

	runtimeOnly("org.postgresql:postgresql:42.7.2")

	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testRuntimeOnly("com.h2database:h2")
}

springBoot {
	buildInfo()
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	reports {
		xml.required.set(false)
		csv.required.set(false)
		html.required.set(true)
		html.outputLocation.set(layout.buildDirectory.dir("reports/coverage") )
	}
}

tasks.named<BootJar>("bootJar") {
	archiveFileName.set("${archiveBaseName.get()}.${archiveExtension.get()}")
}

tasks.register<DockerBuildImage>("dockerBuild") {
	inputDir.set(file("."))
	images.add(dockerImageName)
	buildArgs.put("BUILD_IMAGE", "eclipse-temurin:17.0.11_9-jdk-centos7")
	platform.set("linux/amd64")
	memory.set(549755813888)
}

tasks.register<DockerPushImage>("dockerPush") {
//	dependsOn("dockerBuildImage")
	images.add(dockerImageName)
}
