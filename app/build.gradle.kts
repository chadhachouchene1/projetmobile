plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.biblio"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.biblio"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // SugarORM configuration
        manifestPlaceholders["DATABASE_NAME"] = "biblio.db"
        manifestPlaceholders["DATABASE_VERSION"] = "1"
        manifestPlaceholders["QUERY_LOG"] = "false"
        manifestPlaceholders["DOMAIN_PACKAGE_NAME"] = "com.example.biblio.models"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // SugarORM
    implementation("com.github.satyan:sugar:1.5")
}