plugins {
	java
	jacoco
	checkstyle
	pmd
	id("com.github.spotbugs") version "6.0.6"
	id("org.sonarqube") version "4.4.1.3373"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.meucaixa"
version = "0.0.1-SNAPSHOT"

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
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	implementation("org.liquibase:liquibase-core")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.testcontainers:testcontainers:1.20.6")
	testImplementation("org.testcontainers:junit-jupiter:1.20.6")
	testImplementation("org.testcontainers:postgresql:1.19.0")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

checkstyle {
	toolVersion = "10.12.3"
	configFile = file("config/checkstyle/checkstyle.xml")
	isIgnoreFailures = false
}

pmd {
	toolVersion = "6.55.0"
	ruleSets = listOf() // use regras personalizadas
	ruleSetFiles = files("config/pmd/pmd-rules.xml")
	isIgnoreFailures = false
}

spotbugs {
	toolVersion.set("4.7.3")
	effort.set(com.github.spotbugs.snom.Effort.MAX)
	reportLevel.set(com.github.spotbugs.snom.Confidence.LOW)
}

sonarqube {
	properties {
		property("sonar.projectKey", "api")
		property("sonar.coverage.jacoco.xmlReportPaths", "${buildDir}/reports/jacoco/test/jacocoTestReport.xml")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}