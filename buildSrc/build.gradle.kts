plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("io.papermc.paperweight:paperweight-userdev:1.5.11")
}

repositories {
    gradlePluginPortal()
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}