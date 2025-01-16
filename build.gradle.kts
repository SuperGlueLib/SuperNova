plugins {
    kotlin("jvm") version "1.9.0"
    id("com.gradleup.shadow") version "8.3.5"
}

group = "com.github.mlgpenguin"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://www.jitpack.io")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.4-R0.1-SNAPSHOT")
    implementation("com.github.SuperGlueLib:SuperGlue:1.2.3")
}

kotlin {
    jvmToolchain(17)
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
        minimize()
        dependencies {
            exclude(dependency("org.jetbrains.kotlin::"))
        }
    }

}
