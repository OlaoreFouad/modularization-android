apply {
    from("$rootDir/config/common-kotlin.gradle")
}

plugins {
    id(SqlDelight.plugin)
    kotlin(KotlinPlugins.serialization) version Kotlin.version
}

dependencies {
    "implementation"(project(Modules.heroDomain))

    "implementation"(Ktor.core)
    "implementation"(Ktor.clientSerialization)
    "implementation"(Ktor.android)

    "implementation"(SqlDelight.runtime)
}

sqldelight {
    database("HeroDatabase") {
        packageName = "dev.olaore.hero_datasource.cache"
        sourceFolders = listOf("sqldelight")
    }
}