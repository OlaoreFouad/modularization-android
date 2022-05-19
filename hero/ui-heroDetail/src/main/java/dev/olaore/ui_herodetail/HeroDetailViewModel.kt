package dev.olaore.ui_herodetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.olaore.core.domain.DataState
import dev.olaore.core.domain.Queue
import dev.olaore.core.domain.UIComponent
import dev.olaore.core.util.Logger
import dev.olaore.hero_interactors.usecases.GetHeroUseCase
import dev.olaore.ui_herodetail.ui.state.HeroDetailEvents
import dev.olaore.ui_herodetail.ui.state.HeroDetailEvents.*
import dev.olaore.ui_herodetail.ui.state.HeroDetailState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val getHero: GetHeroUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val logger = Logger("HeroDetailViewModel")
    val state: MutableState<HeroDetailState> = mutableStateOf(HeroDetailState())

    init {
        val heroId = savedStateHandle.get<String>("heroId")
        Integer.parseInt(heroId!!)?.let {
            onTriggerEvent(GetHero(it))
        }
    }

    fun onTriggerEvent(event: HeroDetailEvents) {
        when (event) {
            is GetHero -> {
                getHero(event.id)
            }
            is RemoveMessageFromQueue -> {
                removeTopMessage()
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
                    if (dataState.uiComponent is UIComponent.Dialog) {
                        dispatchMessage(dataState.uiComponent)
                    } else {
                        logger.log((dataState.uiComponent as UIComponent.None).message)
                    }
                }
                is DataState.Data -> {
                    state.value = state.value.copy(hero = dataState.data)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun dispatchMessage(uiComponent: UIComponent) {
        val queue = state.value.errorQueue
        queue.add(uiComponent)
        updateMessageQueue(queue)
    }

    private fun removeTopMessage() {
        try {
            val queue = state.value.errorQueue
            queue.remove()
            updateMessageQueue(queue)
        } catch (ex: Exception) {
            logger.log("dialogqueue is empty!!")
        }
    }

    private fun updateMessageQueue(queue: Queue<UIComponent>) {
        state.value = state.value.copy(errorQueue = Queue(mutableListOf()))
        state.value = state.value.copy(errorQueue = queue)
    }

}
