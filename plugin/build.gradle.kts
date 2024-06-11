plugins {
    id("project.plugin-conventions")
    id("com.github.johnrengelman.shadow") version("7.1.2")
    id("io.papermc.paperweight.userdev")
    application
}

repositories {
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven ("https://repo.essentialsx.net/snapshots/")
    maven("https://repo.minebench.de/")
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

    // Add jackson
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.4")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.17.0")

    // Jackson datatype jdk8
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.13.4")
    // Jackson dataformat
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.4")
    // Jackson module afterburner and parameter names
    implementation("com.fasterxml.jackson.module:jackson-module-afterburner:2.13.4")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.13.4")

    // Add lombok
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
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