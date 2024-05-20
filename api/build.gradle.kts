plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(kotlin("stdlib-jdk8"))
}

group = project.group
version = project.version.toString()

kotlin {
    jvmToolchain {
        (this).languageVersion.set(JavaLanguageVersion.of(21))
    }
    jvmToolchain(21)
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
        kotlinOptions.javaParameters = true
    }
}
repositories {
    mavenCentral()
}