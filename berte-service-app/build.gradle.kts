plugins {
    kotlin("jvm")
    application

    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    alias(libs.plugins.sonarqube)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kover)
}

detekt {
    config.setFrom(files("$rootDir/detekt-config.yml"))
}

dependencies {
    detektPlugins(libs.detekt.ktlint)

    implementation(project(":berte-service-api"))

    implementation(libs.bundles.database)
    implementation(libs.bundles.kafka)
    implementation(libs.bundles.kotlinx)
    implementation(libs.bundles.logback)
    implementation(libs.bundles.micrometer)
    implementation(libs.bundles.postgres)
    implementation(libs.bundles.redis)
    implementation(libs.bundles.rsocket)
    implementation(libs.bundles.shedlock)
    implementation(libs.bundles.web)

    implementation(libs.otel.otlp.exporter)

    testImplementation(libs.bundles.test)
}

configurations.matching { it.name == "detekt" }.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
        }
    }
}

application {
    mainClass.set("ru.somarov.berte.AppKt")
}

ktor {
    fatJar {
        archiveFileName.set("app.jar")
    }
}

tasks {
    shadowJar {
        mergeServiceFiles() // for micrometer reactor context propagation
    }
}

tasks.register("generateBuildInfo") {
    group = "build"
    description = "Generates build-info.properties file with build metadata."

    doLast {
        val buildInfoFile =
            file("${layout.buildDirectory.asFile.get().path}/resources/main/META-INF/build-info.properties")
        buildInfoFile.parentFile.mkdirs()
        buildInfoFile.writeText(
            """
            build.version=${project.version}
            build.group=${project.group}
            build.artifact=${project.name}
        """.trimIndent()
        )
    }
}

tasks.named("jar") {
    dependsOn("generateBuildInfo")
}

tasks.named("buildFatJar") {
    dependsOn("generateBuildInfo")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(
            org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED,
            org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED,
        )
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showExceptions = true
        showCauses = true
        showStackTraces = true
    }
}

kover {
    useJacoco()
    reports {
        verify {
            rule {
                minBound(0)
            }
        }
        total {
            filters {
                excludes {
                    classes(
                        project.properties["test_exclusions"]
                            .toString()
                            .replace("/", ".")
                            .split(",")
                    )
                }
            }

            xml {
                onCheck = true
            }
            html {
                onCheck = true
            }
        }
    }
}

