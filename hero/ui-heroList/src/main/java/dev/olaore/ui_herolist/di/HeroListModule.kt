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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HeroListModule {

    @Singleton
    @Provides
    fun provideSqlDriver(
        @ApplicationContext app: Context
    ): SqlDriver {
        return AndroidSqliteDriver(
            schema = HeroInteractors.schema,
            name = HeroInteractors.dbName,
            context = app,
        )
    }

    @Provides
    @Singleton
    fun provideHeroInteractors(
        sqlDriver: SqlDriver
    ): HeroInteractors {
        return HeroInteractors.build(sqlDriver)
    }

}