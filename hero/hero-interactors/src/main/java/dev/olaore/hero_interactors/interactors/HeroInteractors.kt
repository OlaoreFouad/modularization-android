package dev.olaore.hero_interactors.interactors

import com.squareup.sqldelight.db.SqlDriver
import dev.olaore.core.util.Logger
import dev.olaore.hero_datasource.cache.HeroCache
import dev.olaore.hero_datasource.network.HeroService
import dev.olaore.hero_interactors.usecases.GetHeroUseCase
import dev.olaore.hero_interactors.usecases.GetHerosUseCase

data class HeroInteractors(
    val getHeros: GetHerosUseCase,
    val getHero: GetHeroUseCase
) {

    companion object Factory {

        fun build(
            sqlDriver: SqlDriver
        ): HeroInteractors {
            val service = HeroService.build()
            val cache = HeroCache.build(sqlDriver)
            return HeroInteractors(
                getHeros = GetHerosUseCase(
                    service = service,
                    cache = cache,
                    logger = Logger("MainActivity")
                ),
                getHero = GetHeroUseCase(
                    cache
                )
            )
        }

        val schema: SqlDriver.Schema = HeroCache.schema

        val dbName: String = HeroCache.dbName

    }

}