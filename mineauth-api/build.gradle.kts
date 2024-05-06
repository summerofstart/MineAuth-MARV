dependencies {}

kotlin {
    jvmToolchain {
        (this).languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions.javaParameters = true
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}