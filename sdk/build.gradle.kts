plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("maven-publish")
}

object SDK {
    const val version = "0.1.0"
    const val groupId = "io.teller"
    const val artifactId = "connect-android-sdk"
    const val repository = "github.com/tellerhq/connect-android-prototype"
}

android {
    compileSdk = 33
    buildToolsVersion = "30.0.3"

    namespace = "io.teller.connect.sdk"

    defaultConfig {
        minSdk = 23
        targetSdk = 31

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

    resourcePrefix = "tc_"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.webkit:webkit:1.7.0")
    implementation("androidx.fragment:fragment-ktx:1.6.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("com.beust:klaxon:5.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("Sdk") {

                groupId = SDK.groupId
                artifactId = SDK.artifactId
                version = SDK.version

                from(components["release"])

                pom {
                    name.set("Teller Connect Android SDK")
                    description.set("Connect is a Teller-hosted authorization flow")
                    url.set("https://teller.io")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://${SDK.repository}.git")
                        developerConnection.set("scm:git:ssh://${SDK.repository}.git")
                        url.set("http://${SDK.repository}/")
                    }
                }
            }
        }

        repositories {
            maven {
                name = "GithubPackages"
                url = uri("https://maven.pkg.${SDK.repository}")
                credentials {
                    username =
                        properties["github.user_id"] as String? ?: System.getenv("GITHUB_USER_ID")
                    password = properties["github.access_token"] as String?
                        ?: System.getenv("GITHUB_ACCESS_TOKEN")
                }
            }
        }
    }
}