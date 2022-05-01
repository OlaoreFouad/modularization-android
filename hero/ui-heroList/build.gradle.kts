apply {
    from("$rootDir/config/common-android.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.heroDomain))
    "implementation"(project(Modules.heroInteractors))

    "implementation"(SqlDelight.androidDriver)
}