dependencies {
    val kotlinVersion: String by project
    val jsonVersion: String by project
    val junitVersion: String by project
    val koptionalVersion: String by project
    val resultVersion: String by project

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.json:json:$jsonVersion")
    implementation("com.gojuno.koptional:koptional:$koptionalVersion")
    implementation("com.github.kittinunf.result:result:$resultVersion")

    testImplementation("junit:junit:$junitVersion")
}
