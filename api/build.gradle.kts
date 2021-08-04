import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm")
  id("com.google.devtools.ksp") version "1.5.21-1.0.0-beta06"
}

dependencies {
  api(libs.retrofit.lib)
  ksp(libs.moshix.ksp)
  implementation(libs.moshi.lib)
  implementation(libs.retrofit.moshiConverter) { exclude(group = "com.squareup.moshi") }
  testImplementation(libs.kotlin.coroutines.core)
  testImplementation(kotlin("test-junit"))
  testImplementation(libs.testing.mockWebServer)
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_11.toString()
    languageVersion = "1.5"
  }
}
