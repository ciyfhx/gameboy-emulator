import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinMultiplatform)
}

//group = "com.ciyfhx"
//version = "1.0-SNAPSHOT"

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        val lifecycle_version = "2.8.0-rc01"

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            // ViewModel
            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
            // ViewModel utilities for Compose
            implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")

        }
        desktopMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
            implementation("io.github.oshai:kotlin-logging-jvm:5.1.0")
            implementation("org.slf4j:slf4j-api:2.0.13")
            implementation("ch.qos.logback:logback-classic:1.4.12")
            implementation(compose.desktop.currentOs)
//            testImplementation(kotlin("test"))
        }
    }
}

//tasks.test {
//    useJUnitPlatform()
//}

compose.desktop {
    application {
        mainClass = "com.ciyfhx.emu.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "demo2"
            packageVersion = "1.0.0"
        }
    }
}