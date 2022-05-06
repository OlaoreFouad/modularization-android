package dev.olaore.ui_herodetail

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.olaore.hero_interactors.interactors.HeroInteractors
import dev.olaore.hero_interactors.usecases.GetHeroUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroDetailModule {

    @Provides
    fun provideGetHeroUseCase(
        heroInteractors: HeroInteractors
    ): GetHeroUseCase {
        return heroInteractors.getHero
    }

}