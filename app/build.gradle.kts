plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}


kapt {
    correctErrorTypes = true
}

android {


    compileSdkVersion(29)
    defaultConfig {
        applicationId = "me.emmano.androidmva"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(
            "String",
            "API_KEY",
            "\"9d40db1630da003c1fb826d96bef65eb&hash=b167f09767dfce350bc36a144429c1b5\""
        )
        buildConfigField("String", "BASE_URL", "\"https://gateway.marvel.com/v1/public/\"")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    testOptions.unitTests.isIncludeAndroidResources = true


    dataBinding {
        isEnabled = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        exclude("META-INF/*.kotlin_module")
    }
}

dependencies {
    implementation(project(":common"))
//    implementation("me.emmano:state:0.2.0")
    implementation("me.emmano:adapt:0.1.0")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${rootProject.extra["kotlin_version"]}")
    // GOOGLE: ANDROID
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
    implementation("com.google.android.material:material:1.1.0")


    // GOOGLE: JETPACK (VERSIONS)
    val lifecycle_version = "2.2.0"
    val nav_version = "2.2.1"

    // GOOGLE: JETPACK - VIEWMODEL / LIFECYCLE
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt("androidx.lifecycle:lifecycle-common-java8:$lifecycle_version")

    // GOOGLE: JETPACK - NAVIGATION
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // DEPENDENCY INJECTION
    implementation("org.koin:koin-android:2.0.1")
    implementation("org.koin:koin-android-viewmodel:2.0.1")

    // IMAGE LOADING / CACHING
    implementation("com.github.bumptech.glide:glide:4.10.0")
    kapt("com.github.bumptech.glide:compiler:4.10.0")

//    // RETROFIT FOR SERVICE CALLS
//    implementation("com.squareup.retrofit2:retrofit:2.7.0")
//
//    //TODO Should use Moshi instead of GSON
//    implementation("com.squareup.retrofit2:converter-gson:2.7.0")
//
//    implementation("com.squareup.okhttp3:logging-interceptor:4.2.2")
//
//    //Logging
    implementation("com.jakewharton.timber:timber:4.7.1")

    testImplementation("junit:junit:4.13")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("org.robolectric:robolectric:4.3.1")
    testImplementation("androidx.test:core:1.2.0")
    debugImplementation("androidx.fragment:fragment-testing:1.2.4")
    testImplementation("androidx.test.ext:junit:1.1.1")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.2.2")
    testImplementation("org.koin:koin-test:2.0.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.3")
    testImplementation("androidx.test.espresso:espresso-core:3.2.0")
    testImplementation("androidx.test.espresso:espresso-contrib:3.2.0")


    // -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- //

    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test:core:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
    androidTestImplementation("com.agoda.kakao:kakao:2.0.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("org.koin:koin-test:2.0.1")
}