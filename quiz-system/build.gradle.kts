plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openapi.generator") version "7.1.0"
}

group = "com.yuiko"
version = "0.0.1-SNAPSHOT"

// java {
//     sourceCompatibility = JavaVersion.VERSION_17
//     targetCompatibility = JavaVersion.VERSION_17
// }

sourceSets {
    main {
        java {
            srcDirs += srcDir("$buildDir/generated")
        }
    }
}

repositories {
    mavenCentral()
}

springBoot {
    mainClass.set("com.yuiko.quiz_system.BackendQuizSystemApplicationKt")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.openapitools:openapi-generator-gradle-plugin:7.0.1")
    implementation("io.swagger.core.v3:swagger-annotations:2.2.25")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("io.swagger.core.v3:swagger-core:2.2.27")
    compileOnly("javax.servlet:servlet-api:2.5")
    implementation("org.hibernate.validator:hibernate-validator:8.0.1.Final")

    // jdbc
    implementation("org.liquibase:liquibase-core:4.30.0")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.data:spring-data-jdbc:3.4.2")
    implementation("org.springframework.data:spring-data-relational:3.4.2")
    implementation("org.springframework:spring-context:6.2.2")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("org.postgresql:postgresql:42.7.5")

    // swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

configurations {
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// openapi
openApiValidate {
    inputSpec.set("$rootDir/src/main/resources/openapi/api.yaml")
}
openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/src/main/resources/openapi/api.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("org.yuiko.quiz_system.api")
    invokerPackage.set("org.yuiko.quiz_system.invoker")
    modelPackage.set("org.yuiko.quiz_system.model")
    generateModelTests.set(false)
    generateApiTests.set(false)
}