import org.jmailen.gradle.kotlinter.KotlinterExtension
import org.jmailen.gradle.kotlinter.support.ReporterType

plugins {
    kotlin("jvm") version "1.3.30"
    id("org.jmailen.kotlinter") version "1.24.0"

    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("jacoco")
        plugin("org.jmailen.kotlinter")
        plugin("maven-publish")
        plugin("com.jfrog.bintray")
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/main/kotlin")
    }

    configure<JacocoPluginExtension> {
        toolVersion = "0.8.3"
    }

    tasks.withType<JacocoReport> {
        reports {
            html.isEnabled = false
            xml.isEnabled = true
            csv.isEnabled = false
        }
    }

    configure<KotlinterExtension> {
        reporters = arrayOf(ReporterType.plain.name, ReporterType.checkstyle.name)
    }

    val sourcesJar by tasks.registering(Jar::class) {
        from(sourceSets["main"].allSource)
        archiveClassifier.set("sources")
    }

    val doc by tasks.creating(Javadoc::class) {
        isFailOnError = false
        source = sourceSets["main"].allJava
    }

    version = "1.0.0-alpha1"
    group = "com.github.kittinunf.forge"

    bintray {
        user = findProperty("BINTRAY_USER") as? String
        key = findProperty("BINTRAY_KEY") as? String
        setPublications(project.name)
        with(pkg) {
            repo = "maven"
            name = "Forge"
            desc = "Functional style JSON parsing written in Kotlin"
            userOrg = "kittinunf"
            websiteUrl = "https://github.com/kittinunf/Forge"
            vcsUrl = "https://github.com/kittinunf/Forge"
            setLicenses("MIT")
            with(version) {
                name = "1.0.0-alpha1"
            }
        }
    }

    val javadocJar by tasks.creating(Jar::class) {
        val doc by tasks
        dependsOn(doc)
        from(doc)

        archiveClassifier.set("javadoc")
    }

    publishing {
        publications {
            register(project.name, MavenPublication::class) {
                from(components["java"])
                artifact(sourcesJar)
                artifact(javadocJar)
                groupId = "com.github.kittinunf.forge"
                artifactId = project.name
                version = "1.0.0-alpha1"
                pom {
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("http://www.opensource.org/licenses/mit-license.php")
                        }
                    }
                }
            }
        }
    }
}
