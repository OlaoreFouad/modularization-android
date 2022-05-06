apply {
    from("$rootDir/config/common-android.gradle")
}

dependencies {
    "implementation"(project(Modules.heroInteractors))
    "implementation"(project(Modules.core))
    "implementation"(project(Modules.heroDomain))

    "implementation"(Hilt.android)
    "kapt"(Hilt.compiler)

    "implementation"(Coil.coil)
}