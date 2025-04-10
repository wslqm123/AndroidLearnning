// 文件路径: app/build.gradle.kts
import java.io.File

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

    // 签名配置块
    signingConfigs {
        // 创建名为 "release" 的签名配置
        create("release") {
            // 从环境变量读取 Keystore 相关信息
            // 这些环境变量需要在 CI 环境 (如 GitHub Actions Secrets) 中设置
            val keystorePath = System.getenv("RELEASE_KEYSTORE_PATH")
            val storePassword = System.getenv("RELEASE_STORE_PASSWORD")
            val keyAlias = System.getenv("RELEASE_KEY_ALIAS")
            val keyPassword = System.getenv("RELEASE_KEY_PASSWORD")

            // 检查所有必要信息是否存在，并且 Keystore 文件真实存在
            var keystoreFileExists = false
            if (keystorePath != null) {
                try {
                    // 检查环境变量指定的路径下文件是否存在
                    keystoreFileExists = File(keystorePath).exists()
                } catch (e: Exception) {
                    // 如果检查文件时出错，标记为不存在 (构建将在签名时失败)
                    keystoreFileExists = false
                    println("Error checking keystore file existence at '$keystorePath': ${e.message}") // 可以保留这个错误日志
                }
            }

            // 只有当所有环境变量都已设置，并且 Keystore 文件存在时，才应用签名配置
            if (keystorePath != null && storePassword != null && keyAlias != null && keyPassword != null && keystoreFileExists) {
                println("Applying release signing configuration using keystore at: $keystorePath") // 确认配置应用的日志
                // 将读取到的值赋给签名配置的属性
                storeFile = file(keystorePath) // 使用 file() 将路径字符串转换为 File 对象
                this.storePassword = storePassword
                this.keyAlias = keyAlias
                this.keyPassword = keyPassword
            } else {
                // 如果缺少任何信息或文件不存在，此签名配置将无效。
                // Gradle 在尝试使用此配置进行签名时会报错，这通常是期望的行为。
                println("Warning: Release signing configuration incomplete or keystore file not found. Necessary environment variables: RELEASE_KEYSTORE_PATH, RELEASE_STORE_PASSWORD, RELEASE_KEY_ALIAS, RELEASE_KEY_PASSWORD. Also ensure the file at RELEASE_KEYSTORE_PATH exists.")
            }
        }
    }

    buildTypes {
        release {
            // 对 release 构建类型应用上面定义的 "release" 签名配置
            signingConfig = signingConfigs.getByName("release")

            // 开启代码混淆和优化 (推荐用于 Release 构建)
            isMinifyEnabled = true
            // 开启资源压缩 (移除未使用的资源，推荐)
            isShrinkResources = true

            // 指定 ProGuard 规则文件
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // 如果有特定于 release 的规则，可以添加，例如：
            // proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "proguard-release.pro")
        }
        debug {
            // Debug 构建类型通常不需要特殊签名配置 (使用默认的 debug key)
            isMinifyEnabled = false
        }
    }

    // Java/Kotlin 编译选项
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true // 这个 buildFeature 标志仍然是启用 Compose UI 工具链所必需的
    }
}


// 依赖项
dependencies {
    // --- 保留你项目的所有依赖项 ---
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
    // --- 结束依赖项 ---
}