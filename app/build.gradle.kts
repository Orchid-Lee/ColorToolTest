plugins {
    autowire(libs.plugins.android.application)
    autowire(libs.plugins.kotlin.android)
    autowire(libs.plugins.kotlin.ksp)
}

android {
    namespace = property.project.app.packageName
    compileSdk = property.project.android.compileSdk

    defaultConfig {
        applicationId = property.project.app.packageName
        minSdk = property.project.android.minSdk
        targetSdk = property.project.android.targetSdk
        versionName = property.project.app.versionName
        versionCode = property.project.app.versionCode
        namespace = property.project.app.packageName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    androidResources {
        localeFilters += "zh" // 使用逗号分隔的字符串
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a")
            isUniversalApk = true
        }
    }

    packaging {
        resources {
            excludes += "DebugProbesKt.bin"
            excludes += "kotlin-tooling-metadata.json"
            excludes += "META-INF/*.kotlin_module"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf(
            "-Xno-param-assertions",
            "-Xno-call-assertions",
            "-Xno-receiver-assertions"
        )
    }

    signingConfigs {
        create("release") {
            if (System.getenv("CI") != null) {
                storeFile = file(System.getenv("TWICEYUAN_KEYSTORE"))
                storePassword = System.getenv("TWICEYUAN_KEYSTORE_PASSWD")
                keyAlias = System.getenv("TWICEYUAN_KEY_ALIAS")
                keyPassword = System.getenv("TWICEYUAN_KEY_PASSWD")
            } else {
                val keystore = "/test/test/test/xxx/ANDROID_SIGN.jks"
                storeFile = file(keystore ?: "/dev/null")
                storePassword = "123456"
                keyAlias = "ANDROID_SIGN"
                keyPassword = "123456"
            }
        }
    }

    buildTypes {
        debug {
            // debug 构建类型的配置
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }


    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
    lint { checkReleaseBuilds = false }
    // TODO Please visit https://highcapable.github.io/YukiHookAPI/en/api/special-features/host-inject
    // TODO 请参考 https://highcapable.github.io/YukiHookAPI/zh-cn/api/special-features/host-inject
    // androidResources.additionalParameters += listOf("--allow-reserved-package-id", "--package-id", "0x64")
}

dependencies {
    compileOnly(de.robv.android.xposed.api)
    implementation(com.highcapable.yukihookapi.api)
    ksp(com.highcapable.yukihookapi.ksp.xposed)
    implementation ("org.luckypray:dexkit:2.0.3")
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:2.1.20")
    implementation ("androidx.documentfile:documentfile:1.1.0")
    implementation ("androidx.activity:activity-ktx:1.10.1")
    testImplementation(junit.junit)
    androidTestImplementation(androidx.test.ext.junit)
    androidTestImplementation(androidx.test.espresso.espresso.core)
}