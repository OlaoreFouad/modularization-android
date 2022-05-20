package dev.olaore.hero_datasource_test.network

import dev.olaore.hero_datasource.network.models.HeroResponse
import dev.olaore.hero_datasource.network.models.toHero
import dev.olaore.hero_domain.Hero
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val json = Json {
    ignoreUnknownKeys = true
}

fun serializeHeroData(jsonData: String): List<Hero>{
    return json.decodeFromString<List<HeroResponse>>(jsonData).map { it.toHero() }
}
