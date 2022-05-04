package dev.olaore.hero_interactors.usecases

import dev.olaore.core.domain.DataState
import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.UIComponent
import dev.olaore.hero_datasource.cache.HeroCache
import dev.olaore.hero_domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

class GetHeroUseCase(
    private val cache: HeroCache
) {

    fun execute(heroId: Int): Flow<DataState<Hero>> = flow {

        try {
            emit(DataState.Loading(ProgressBarState.Loading))

            val cachedHero = cache.getHero(heroId)
                ?: throw Exception("This hero does not exist in the cache.")

            emit(DataState.Data(cachedHero))

        } catch (ex: Throwable) {
            ex.printStackTrace()
            emit(
                DataState.Response(
                    uiComponent = UIComponent.Dialog(
                        title = "Error Occurred",
                        description = ex.message ?: "unknown error"
                    )
                )
            )
        } finally {
            emit(DataState.Loading(ProgressBarState.Idle))
        }

    }

}