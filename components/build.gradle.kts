apply {
    from("$rootDir/config/common-android.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
}
