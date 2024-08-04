@file:Suppress("UnstableApiUsage")

rootProject.name = "berte-service"

include("berte-service-app", "berte-service-api")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}