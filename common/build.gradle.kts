import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
//    kotlin("native.cocoapods")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("JokesDatabase") {
        packageName = "me.emmano.androidmva"
    }
}

version = "1.0.0"

val coroutinesVersion = "1.3.9"
val ktorVersion = "1.4.1"
val sqlDelightVersion = "1.3.0"

kotlin {
//    cocoapods {
//        summary = "Shared module for Android and iOS"
//        homepage = "Link to a Kotlin/Native module homepage"
//    }

    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        compilations {
            val main by getting {
                kotlinOptions.freeCompilerArgs = listOf("-Xobjc-generics")
            }
        }
    }
    android()


    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
        implementation("io.ktor:ktor-client-json:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization:$ktorVersion")
        implementation("io.ktor:ktor-client-core:$ktorVersion")
        implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")
        implementation("io.ktor:ktor-client-logging:$ktorVersion")
    }

    sourceSets["commonTest"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test-common:1.4.0")
        implementation("org.jetbrains.kotlin:kotlin-test-annotations-common:1.4.0")
//        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    }

    sourceSets["iosMain"].dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
        implementation("io.ktor:ktor-client-json:$ktorVersion")
        implementation("io.ktor:ktor-client-serialization:$ktorVersion")
        implementation("io.ktor:ktor-client-ios:$ktorVersion")
//        implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
        implementation("io.ktor:ktor-client-logging:$ktorVersion")

    }

    sourceSets["iosTest"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-test:1.4.0")
    }

    sourceSets["androidTest"].dependencies {
        implementation("junit:junit:4.13")
        implementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.0")
        implementation("org.jetbrains.kotlin:kotlin-test:1.4.0")
    }

}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("io.ktor:ktor-client-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-android:$ktorVersion")
    implementation("io.ktor:ktor-client-logging-jvm:$ktorVersion")
    implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
    implementation("org.slf4j:slf4j-android:1.7.30")

    // LiveData and ViewModel
    val lifecycleVersion = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
