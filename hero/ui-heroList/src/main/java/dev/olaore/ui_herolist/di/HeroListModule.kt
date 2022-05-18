package dev.olaore.ui_herolist.di

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.olaore.hero_interactors.interactors.HeroInteractors
import dev.olaore.hero_interactors.usecases.FilterHerosUseCase
import dev.olaore.hero_interactors.usecases.GetHerosUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroListModule {

    @Provides
    fun provideGetHeros(
        heroInteractors: HeroInteractors
    ): GetHerosUseCase {
        return heroInteractors.getHeros
    }

    @Provides
    fun provideFilterHeros(
        heroInteractors: HeroInteractors
    ): FilterHerosUseCase {
        return heroInteractors.filterHeros
    }

}
