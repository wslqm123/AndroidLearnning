plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.lqm.androidlearnning"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.lqm.androidlearnning"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }

    signingConfigs {
        create("release") {
            // 优先从环境变量读取
            val keystorePath = System.getenv("RELEASE_KEYSTORE_PATH")
            val storePassword = System.getenv("RELEASE_STORE_PASSWORD")
            val keyAlias = System.getenv("RELEASE_KEY_ALIAS")
            val keyPassword = System.getenv("RELEASE_KEY_PASSWORD")

            if (keystorePath != null && storePassword != null && keyAlias != null && keyPassword != null && File(keystorePath).exists()) {
                storeFile = file(keystorePath)
                this.storePassword = storePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            } else {
                println("Warning: Release signing information not fully provided via environment variables or keystore file not found.")
                // 可以设置一个默认的 debug 签名或者让构建失败
                // signingConfig = signingConfigs.getByName("debug") // 例如，回退到 debug 签名
            }
        }
    }

    buildTypes {
        release {
            // ... 其他 release 配置 ...
            signingConfig = signingConfigs.getByName("release") // 关联签名配置
            isMinifyEnabled = true // 通常 Release 包会开启混淆
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            // ... debug 配置 ...
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}