apply {
    from("$rootDir/config/common-android.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.heroDomain))
    "implementation"(project(Modules.heroInteractors))
    "implementation"(project(Modules.components))

    "implementation"(SqlDelight.androidDriver)

    "implementation"(Coil.coil)

    "implementation"(Hilt.android)
    "kapt"(Hilt.compiler)
}
