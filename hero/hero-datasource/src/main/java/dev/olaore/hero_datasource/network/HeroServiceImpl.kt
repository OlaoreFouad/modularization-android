package dev.olaore.hero_datasource.network

import dev.olaore.hero_datasource.network.constants.Endpoints
import dev.olaore.hero_datasource.network.models.HeroResponse
import dev.olaore.hero_datasource.network.models.toHero
import dev.olaore.hero_domain.Hero
import io.ktor.client.*
import io.ktor.client.request.*

class HeroServiceImpl(
    private val httpClient: HttpClient
) : HeroService {

    override suspend fun getHeros(): List<Hero> {
        return this.httpClient.get<List<HeroResponse>> {
            url(Endpoints.HERO_STATS)
        }.map { it.toHero() }
    }

}