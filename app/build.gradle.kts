plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.nearbyplacesfinder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.nearbyplacesfinder"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
dependencies{



    dependencies {
        implementation("org.osmdroid:osmdroid-android:6.1.14")
        implementation("com.android.volley:volley:1.2.1")

        implementation(libs.appcompat)
        implementation(libs.material)
        implementation(libs.activity)
        implementation(libs.constraintlayout)

        testImplementation(libs.junit)
        androidTestImplementation(libs.ext.junit)
        androidTestImplementation(libs.espresso.core)
    }

}