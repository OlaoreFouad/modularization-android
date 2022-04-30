package dev.olaore.hero_interactors.interactors

import dev.olaore.hero_datasource.network.HeroService
import dev.olaore.hero_interactors.usecases.GetHerosUseCase

data class HeroInteractors(
    val getHeros: GetHerosUseCase
) {

    companion object Factory {

        fun build(): HeroInteractors {
            val service = HeroService.build()
            return HeroInteractors(
                getHeros = GetHerosUseCase(
                    service = service
                )
            )
        }

    }

}