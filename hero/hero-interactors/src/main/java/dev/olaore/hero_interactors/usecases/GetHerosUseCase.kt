package dev.olaore.hero_interactors.usecases

import dev.olaore.core.domain.DataState
import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.UIComponent
import dev.olaore.hero_datasource.network.HeroService
import dev.olaore.hero_domain.Hero
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay

class GetHerosUseCase(
    private val service: HeroService
) {

    fun execute(): Flow<DataState<List<Hero>>> = flow {

        try {
            emit(DataState.Loading(ProgressBarState.Loading))

            delay(1000)

            val heros: List<Hero> = try {
                service.getHeros()
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
                listOf()
            }
            emit(DataState.Data(heros))
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