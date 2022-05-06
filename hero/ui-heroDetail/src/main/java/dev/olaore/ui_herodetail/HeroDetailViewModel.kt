package dev.olaore.ui_herodetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.olaore.core.domain.DataState
import dev.olaore.hero_interactors.usecases.GetHeroUseCase
import dev.olaore.ui_herodetail.ui.state.HeroDetailEvents
import dev.olaore.ui_herodetail.ui.state.HeroDetailState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val getHero: GetHeroUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state: MutableState<HeroDetailState> = mutableStateOf(HeroDetailState())

    init {
        val heroId = savedStateHandle.get<String>("heroId")
        Integer.parseInt(heroId!!)?.let {
            onTriggerEvent(HeroDetailEvents.GetHero(it))
        }
    }

    private fun onTriggerEvent(event: HeroDetailEvents) {
        when (event) {
            is HeroDetailEvents.GetHero -> {
                getHero(event.id)
            }
        }
    }

    private fun getHero(id: Int) {
        getHero.execute(id).onEach { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    state.value = state.value.copy(
                        progressBarState = dataState.state
                    )
                }
                is DataState.Response -> {
                    // TODO: handle error cases.
                }
                is DataState.Data -> {
                    state.value = state.value.copy(hero = dataState.data)
                }
            }
        }.launchIn(viewModelScope)
    }

}