
apply {
    from("$rootDir/config/common-kotlin.gradle")
}

dependencies {
    "implementation"(project(Modules.core))
}
