apply {
    from("$rootDir/config/common-kotlin.gradle")
}

dependencies {
    "implementation"(project(Modules.heroDomain))
    "implementation"(project(Modules.heroDataSource))
    "implementation"(project(Modules.core))

    "implementation"(Kotlinx.coroutinesCore)
}