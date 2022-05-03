
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath(Build.sqlDelightGradlePlugin)
        classpath(Build.hiltAndroid)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}