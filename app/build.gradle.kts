// ملف Gradle الخاص بوحدة التطبيق (app)
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.halaqa.quran"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.halaqa.quran"
        minSdk = 23          // يدعم أجهزة أندرويد المتوسطة والقديمة (Android 6+)
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        // دعم اللغة العربية كلغة افتراضية للتطبيق
        resourceConfigurations += listOf("ar")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // -------- Compose (Jetpack Compose + Material 3) --------
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // -------- Room (قاعدة بيانات محلية) --------
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // -------- Core --------
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.core:core-splashscreen:1.0.1")

    // ملاحظة: لا نستخدم أي مكتبة PDF أو Charts خارجية بناءً على اختيار
    // "أخف وزناً" — نعتمد على android.graphics.pdf.PdfDocument المدمج
    // ورسوم بيانية مرسومة يدويًا عبر Compose Canvas.

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
