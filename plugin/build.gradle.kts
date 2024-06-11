plugins {
    id("project.plugin-conventions")
    id("com.github.johnrengelman.shadow") version("7.1.2")
    id("io.papermc.paperweight.userdev")
    application
}

repositories {
    mavenCentral()
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.essentialsx.net/snapshots/")
    maven("https://repo.minebench.de/")
    maven("https://repo.spongepowered.org/repository/maven-public/")
}

dependencies {
    api(project(":api"))

    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    // PAPER COMPLETE
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")

    compileOnly("io.github.miniplaceholders:miniplaceholders-api:2.1.0")

    compileOnly("me.clip:placeholderapi:2.11.5")

    // Add essentials
    compileOnly("net.essentialsx:EssentialsX:2.21.0-SNAPSHOT")

    // Configurate
    api("org.spongepowered:configurate-yaml:4.1.2")

    // Add lombok
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

application {
    mainClass.set("com.wasmake.itemupgrade.ItemUpgrade")
}

tasks {

    shadowJar {
        archiveBaseName.set("itemupgrade")
        archiveClassifier.set("")
    }
}