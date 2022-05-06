package dev.olaore.dotainfo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.olaore.core.domain.DataState
import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.UIComponent
import dev.olaore.core.util.Logger
import dev.olaore.dotainfo.state.HeroListEvents
import dev.olaore.dotainfo.state.HeroListEvents.*
import dev.olaore.hero_interactors.interactors.HeroInteractors
import dev.olaore.hero_interactors.usecases.GetHerosUseCase
import dev.olaore.ui_herolist.state.HeroListState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HeroListViewModel @Inject constructor (
    private val getHeros: GetHerosUseCase
) : ViewModel() {

    private val logger = Logger("MainActivity")
    val state: MutableState<HeroListState> = mutableStateOf(HeroListState())

    init {
        onTriggerEvent(GetHeros)
    }

    private fun onTriggerEvent(event: HeroListEvents) {
        when (event) {
            is GetHeros -> getHeros()
        }
    }

    private fun getHeros() {
        getHeros.execute().onEach {
            when (it) {
                is DataState.Response -> {
                    if (it.uiComponent is UIComponent.Dialog) {
                        logger.log((it.uiComponent as UIComponent.Dialog).description)
                    } else {
                        logger.log((it.uiComponent as UIComponent.None).message)
                    }
                }
                is DataState.Data -> {
                    state.value = state.value.copy(
                        heros = it.data ?: listOf(),
                        progressBarState = ProgressBarState.Idle
                    )
                }
                is DataState.Loading -> {
                    state.value = state.value.copy(
                        progressBarState = it.state,
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}