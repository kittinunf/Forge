import com.jfrog.bintray.gradle.BintrayExtension
import org.jmailen.gradle.kotlinter.support.ReporterType

plugins {
    kotlin("jvm") version "1.4.0"
    id("org.jmailen.kotlinter") version "3.0.2"

    jacoco
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
        plugin("org.jmailen.kotlinter")
        plugin("jacoco")
        plugin("maven-publish")
        plugin("com.jfrog.bintray")
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/main/kotlin")
    }

    jacoco {
        val jacocoVersion: String by project
        toolVersion = jacocoVersion
    }

    tasks.withType<JacocoReport> {
        reports {
            html.isEnabled = false
            xml.isEnabled = true
            csv.isEnabled = false
        }
    }

    kotlinter {
        reporters = arrayOf(ReporterType.plain.name, ReporterType.checkstyle.name)
    }

    val artifactRepo: String by project
    val artifactName: String by project
    val artifactDesc: String by project
    val artifactUserOrg: String by project
    val artifactUrl: String by project
    val artifactScm: String by project
    val artifactLicenseName: String by project
    val artifactLicenseUrl: String by project

    val artifactPublish: String by project
    val artifactGroupId: String by project
    version = artifactPublish
    group = artifactGroupId

    //publishing
    configure<PublishingExtension> {

        val sourceSets = project.the<SourceSetContainer>()

        val sourcesJar by tasks.registering(Jar::class) {
            from(sourceSets["main"].allSource)
            classifier = "sources"
        }

        val javadocJar by tasks.creating(Jar::class) {
            val doc by tasks.creating(Javadoc::class) {
                isFailOnError = false
                source = sourceSets["main"].allJava
            }

            dependsOn(doc)
            from(doc)

            classifier = "javadoc"
        }

        publications {
            register(project.name, MavenPublication::class) {
                from(components["java"])
                artifact(sourcesJar.get())
                artifact(javadocJar)
                groupId = artifactGroupId
                artifactId = project.name
                version = artifactPublish

                pom {
                    name.set(project.name)
                    description.set(artifactDesc)

                    packaging = "jar"
                    url.set(artifactUrl)

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("http://www.opensource.org/licenses/mit-license.php")
                        }
                    }

                    developers {
                        developer {
                            name.set("kittinunf")
                        }
                        developer {
                            name.set("babedev")
                        }
                        developer {
                            name.set("janjaali")
                        }
                    }

                    contributors {
                        // https://github.com/kittinunf/Result/graphs/contributors
                        contributor {
                            name.set("pgreze")
                        }
                    }

                    scm {
                        url.set(artifactUrl)
                        connection.set(artifactScm)
                        developerConnection.set(artifactScm)
                    }
                }
            }
        }
    }

    // bintray
    configure<BintrayExtension> {
        user = findProperty("BINTRAY_USER") as? String
        key = findProperty("BINTRAY_KEY") as? String
        setPublications(project.name)
        publish = true
        pkg.apply {
            repo = artifactRepo
            name = artifactName
            desc = artifactDesc
            userOrg = artifactUserOrg
            websiteUrl = artifactUrl
            vcsUrl = artifactUrl
            setLicenses(artifactLicenseName)
            version.apply {
                name = artifactPublish
                gpg(delegateClosureOf<BintrayExtension.GpgConfig> {
                    sign = true
                    passphrase = System.getenv("GPG_PASSPHRASE") ?: ""
                })
            }
        }
    }

    // jacoco
    configure<JacocoPluginExtension> {
        toolVersion = extra.get("jacoco") as String
    }

    tasks.withType<JacocoReport> {
        reports {
            html.isEnabled = true
            xml.isEnabled = true
            csv.isEnabled = false
        }
    }
}

fun <T> NamedDomainObjectContainer<T>.release(configure: T.() -> Unit) = getByName("release", configure)
fun <T> NamedDomainObjectContainer<T>.debug(configure: T.() -> Unit) = getByName("debug", configure)
fun <T> NamedDomainObjectContainer<T>.all(configure: T.() -> Unit) = getByName("all", configure)
