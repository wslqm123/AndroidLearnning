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
            // --- 新增日志 ---
            println("--- Signing Config Debug ---")
            val keystorePath = System.getenv("RELEASE_KEYSTORE_PATH")
            println("Env RELEASE_KEYSTORE_PATH: $keystorePath")
            val storePassword = System.getenv("RELEASE_STORE_PASSWORD")
            println("Env RELEASE_STORE_PASSWORD provided: ${storePassword != null}") // 不直接打印密码
            val keyAlias = System.getenv("RELEASE_KEY_ALIAS")
            println("Env RELEASE_KEY_ALIAS: $keyAlias")
            val keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
            println("Env RELEASE_KEY_PASSWORD provided: ${keyPassword != null}") // 不直接打印密码

            var keystoreFileExists = false
            if (keystorePath != null) {
                try {
                    keystoreFileExists = File(keystorePath).exists()
                    println("Checking file existence at '$keystorePath': $keystoreFileExists")
                } catch (e: Exception) {
                    println("Error checking file existence at '$keystorePath': ${e.message}")
                }
            } else {
                println("Keystore path is null, cannot check file existence.")
            }
            println("--------------------------")
            // --- 结束新增日志 ---


            if (keystorePath != null && storePassword != null && keyAlias != null && keyPassword != null && keystoreFileExists) {
                println("All signing information seems valid, applying signing config.") // 新增成功日志
                storeFile = file(keystorePath)
                this.storePassword = storePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            } else {
                println("Warning: Release signing information not fully provided via environment variables or keystore file not found.") // 保留现有警告
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