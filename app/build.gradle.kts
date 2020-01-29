plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(Apps.compileSdk)

    defaultConfig {
        applicationId = "de.istomov.cats"
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Libs.kotlin)
    implementation(Libs.appcompat)
    implementation(Libs.material)
    implementation(Libs.coreKtx)
    implementation(Libs.constraintLayout)
    implementation(Libs.lifecycleExtensions)
    implementation(Libs.lifecycleViewModelKtx)

    implementation(Libs.rxJava)
    implementation(Libs.rxKotlin)
    implementation(Libs.rxAndroid)

    implementation(Libs.koin)
    implementation(Libs.picasso)
    implementation(Libs.retrofit)
    implementation(Libs.retrofitRxJava)
    implementation(Libs.retrofitMoshi)
    implementation(Libs.okHttp)
    implementation(Libs.okHttpLogging)
    implementation(Libs.moshi)
    implementation(Libs.timber)

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
