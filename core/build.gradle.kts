import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    java
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.resource.factory)
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
    maven("https://repo.codemc.io/repository/maven-public/")
}

group = "party.morino"
version = project.version.toString()

dependencies {
    implementation(project(":api"))
    compileOnly(libs.paper.api)

    implementation(libs.bundles.commands)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.coroutines)

    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)

    implementation(libs.bundles.securities)

    implementation(libs.bundles.exposed)

    implementation(libs.koin.core)
    implementation(kotlin("stdlib-jdk8"))

    compileOnly(libs.vault.api)
    compileOnly(libs.quickshop.api)
}

kotlin {
    jvmToolchain {
        (this).languageVersion.set(JavaLanguageVersion.of(21))
    }
    jvmToolchain(21)
}

tasks {
    compileKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
        compilerOptions.javaParameters = true
        compilerOptions.languageVersion.set(KotlinVersion.KOTLIN_2_0)
    }
    compileTestKotlin {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }
    build {
        dependsOn("shadowJar")
    }
    shadowJar
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }
    runServer {
        minecraftVersion("1.20.6")
        val plugins = runPaper.downloadPluginsSpec {
            //Vault
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
            //EssestialsX
            url("https://ci.ender.zone/job/EssentialsX/1576/artifact/jars/EssentialsX-2.21.0-dev+93-3a6fdd9.jar")
            //QuickShop
            url("https://cdn.modrinth.com/data/ijC5dDkD/versions/yr8al7fH/QuickShop-Hikari-6.2.0.6.jar")
        }
        downloadPlugins {
            downloadPlugins.from(plugins)
        }
    }
}


sourceSets.main {
    resourceFactory {
        bukkitPluginYaml {
            name = rootProject.name
            version = project.version.toString()
            website = "https://github.com/morinoparty/Moripa-API"
            main = "$group.mineauth.core.MineAuth"
            apiVersion = "1.20"
            libraries = libs.bundles.coroutines.asString()
            softDepend = listOf("Vault", "QuickShop-Hikari")
        }
    }
}

fun Provider<MinimalExternalModuleDependency>.asString(): String {
    val dependency = this.get()
    return dependency.module.toString() + ":" + dependency.versionConstraint.toString()
}

fun Provider<ExternalModuleDependencyBundle>.asString(): List<String> {
    return this.get().map { dependency ->
        "${dependency.group}:${dependency.name}:${dependency.version}"
    }
}

