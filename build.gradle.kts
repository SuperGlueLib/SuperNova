plugins {
    kotlin("jvm") version "1.9.0"
    id("com.gradleup.shadow") version "8.3.5"
    `maven-publish`
}

group = "com.github.supergluelib"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
//    maven("https://www.jitpack.io")

    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    api("com.github.supergluelib:SuperGlue:1.2.4")

}

kotlin {
    jvmToolchain(17)
}

java {
    withJavadocJar()
    withSourcesJar()
}

tasks {
    processResources {
        val props = LinkedHashMap(mapOf("version" to version, "project_name" to rootProject.name))
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand (props)
        }
    }
    shadowJar {
//        minimize()  // Used as dependency
        dependencies {
            exclude(dependency("org.jetbrains.kotlin::"))
        }
    }
}

publishing.publications.create<MavenPublication>("maven") {
    groupId = group.toString()
    artifactId = "supernova"
    version = version.toString()
    from(components["java"])
}

