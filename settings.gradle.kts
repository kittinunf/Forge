pluginManagement {
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven(url = "https://plugins.gradle.org/m2/")
    }
    resolutionStrategy {
    }
}

val projects = listOf(":forge")

include(*(projects.toTypedArray()))
