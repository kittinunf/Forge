dependencies {
    val kotlinVersion: String by project
    val jsonVersion: String by project
    val junitVersion: String by project

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.json:json:$jsonVersion")

    testImplementation("junit:junit:$junitVersion")
}
