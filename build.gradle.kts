import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.plugin.yml)
}

group = "party.morino"
version = "1.0-SNAPSHOT"

fun captureVersion(dependency: Dependency): String {
    return dependency.version ?: throw IllegalArgumentException("Version not found for $dependency")
}


repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://plugins.gradle.org/m2/")
    maven("https://repo.incendo.org/content/repositories/snapshots")
    maven("https://repo.codemc.io/repository/maven-public/")
}


dependencies {

    compileOnly(libs.paper.api)

    library(kotlin("stdlib"))

    implementation(libs.lamp.common)
    implementation(libs.lamp.bukkit)


    implementation(libs.kotlinx.serialization.json)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.mccoroutine.bukkit.api)
    implementation(libs.mccoroutine.bukkit.core)

    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.contentNegotiation)
    implementation(libs.ktor.serialization.kotlinxJson)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.authJwt)
    implementation(libs.ktor.network.tlsCertificates)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.java)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.server.velocity)

    implementation(libs.bcpkix.jdk18on)
    implementation(libs.bcprov.jdk18on)

    implementation(libs.password4j)

    implementation(libs.nimbus.jose.jwt)

    implementation(libs.logback.classic)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)

    implementation(libs.koin.core)
}

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
    build {
        dependsOn(shadowJar)
    }
    runServer {
        minecraftVersion("1.20.4")
    }
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
    test {
        useJUnitPlatform()
        testLogging {
            showStandardStreams = true
            events("passed", "skipped", "failed")
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
}



bukkit {
    name = "Moripa-API"
    version = "miencraft_plugin_version"
    website = "https://github.com/morinoparty/Moripa-API"

    main = "$group.moripaapi.MoripaAPI"

    apiVersion = "1.20"

    libraries = listOf(
        "com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.15.0",
        "com.github.shynixn.mccoroutine:mccoroutine-bukkit-core:2.15.0",
    )
}

configurations {
    create("resolvable") {
        extendsFrom(configurations["default"])
        isCanBeResolved = true
    }
}

tasks.register("depsize") {
    description = "Prints dependencies for \"default\" configuration"
    doLast {
        listConfigurationDependencies(configurations["resolvable"])
    }
}

tasks.register("depsize-all-configurations") {
    description = "Prints dependencies for all available configurations"
    doLast {
        configurations.filter { it.isCanBeResolved }.forEach { listConfigurationDependencies(it) }
    }
}

fun listConfigurationDependencies(configuration: Configuration) {
    val formatStr = "%,10.2f"
    val size = configuration.sumOf { it.length() / (1024.0 * 1024.0) }
    val out = StringBuffer()
    out.append("\nConfiguration name: \"${configuration.name}\"\n")
    if (size > 0) {
        out.append("Total dependencies size:".padEnd(65))
        out.append("${String.format(formatStr, size)} Mb\n\n")

        configuration.sortedBy { -it.length() }.forEach {
            out.append(it.name.padEnd(65))
            out.append("${String.format(formatStr, (it.length() / (1024.0 * 1024.0)))} mb\n")
        }
    } else {
        out.append("No dependencies found")
    }
    println(out)
}