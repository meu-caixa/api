import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

plugins {
	java
	jacoco
	checkstyle
	pmd
//	id("com.github.spotbugs") version "6.1.9"
	id("org.sonarqube") version "6.0.1.5171"
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

sonar {
	properties {
		property("sonar.projectKey", "br.com.meucaixa")
		property("sonar.organization", "meu-caixa")
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.coverage.jacoco.xmlReportPaths", "${buildDir}/reports/jacoco/test/jacocoTestReport.xml")
		property("sonar.token", System.getenv("SONAR_TOKEN"))
	}
}

checkstyle {
	toolVersion = "10.12.4"
	configFile = rootProject.file("${rootDir}/config/checkstyle/checkstyle.xml")
}

pmd {
	isConsoleOutput = true
	toolVersion = "7.0.0"
	rulesMinimumPriority = 3
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<Checkstyle>().configureEach {
	reports {
		xml.required = false
		html.required = true
		html.stylesheet = resources.text.fromFile("config/xsl/checkstyle.xsl")
	}
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)

	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}

tasks.register("printCoverage") {
	dependsOn(tasks.jacocoTestReport)

	doLast {
		val reportFile = file("${buildDir}/reports/jacoco/test/jacocoTestReport.xml")
		if (reportFile.exists()) {
			val documentBuilderFactory = DocumentBuilderFactory.newInstance()
			documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
			documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false)

			val documentBuilder = documentBuilderFactory.newDocumentBuilder()
			val document = documentBuilder.parse(reportFile)
			val counters = document.getElementsByTagName("counter")
			var covered = 0.0
			var missed = 0.0

			for (i in 0 until counters.length) {
				val counter = counters.item(i) as Element
				if (counter.getAttribute("type") == "INSTRUCTION") {
					covered = counter.getAttribute("covered").toDouble()
					missed = counter.getAttribute("missed").toDouble()
					break
				}
			}
			val total = covered + missed
			val percentage = if (total > 0) (covered / total) * 100 else 0.0
			println("Instruction Coverage: %.2f%%".format(percentage))
		} else {
			println("Coverage report not found.")
		}
	}
}