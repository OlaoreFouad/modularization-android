package dev.olaore.dotainfo.di

import android.content.Context
import coil.ImageLoader
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.olaore.hero_interactors.interactors.HeroInteractors
import dev.olaore.ui_herolist.R
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideImageLoader(
        @ApplicationContext app: Context
    ): ImageLoader {
        return ImageLoader.Builder(app)
            .placeholder(R.drawable.white_background)
            .error(R.drawable.error_image)
            .crossfade(true)
            .availableMemoryPercentage(.25)
            .build()
    }

}