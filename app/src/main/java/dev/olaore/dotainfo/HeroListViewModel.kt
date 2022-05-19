package dev.olaore.dotainfo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.olaore.core.domain.DataState
import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.Queue
import dev.olaore.core.domain.UIComponent
import dev.olaore.core.util.Logger
import dev.olaore.hero_domain.HeroAttribute
import dev.olaore.hero_domain.HeroFilter
import dev.olaore.hero_interactors.usecases.FilterHerosUseCase
import dev.olaore.ui_herolist.state.HeroListEvents
import dev.olaore.ui_herolist.state.HeroListEvents.*
import dev.olaore.hero_interactors.usecases.GetHerosUseCase
import dev.olaore.ui_herolist.state.HeroListState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HeroListViewModel @Inject constructor(
    private val getHeros: GetHerosUseCase,
    private val filterHeros: FilterHerosUseCase
) : ViewModel() {

    private val logger = Logger("HeroListViewModel")
    val state: MutableState<HeroListState> = mutableStateOf(HeroListState())

    init {
        onTriggerEvent(GetHeros)
    }

    fun onTriggerEvent(event: HeroListEvents) {
        when (event) {
            is GetHeros -> getHeros()
            is FilterHeros -> {
                filterHeros()
            }
            is UpdateFilterQuery -> {
                updateFilterQuery(event.query)
            }
            is UpdateHeroFilter -> {
                updateHeroFilter(event.heroFilter)
            }
            is UpdateFilterDialogState -> {
                state.value = state.value.copy(
                    filterDialogState = event.dialogState
                )
            }
            is UpdateHeroAttributeFilter -> {
                updateHeroAttributeFilter(event.attribute)
            }
            is RemoveMessageFromQueue -> {
                removeTopMessage()
            }
        }
    }

    private fun updateHeroAttributeFilter(
        attributeFilter: HeroAttribute
    ) {
        state.value = state.value.copy(
            filterPrimaryAttribute = attributeFilter
        )
        filterHeros()
    }

    private fun updateHeroFilter(heroFilter: HeroFilter) {
        state.value = state.value.copy(
            heroFilter = heroFilter
        )
        filterHeros()
    }

    private fun filterHeros() {
        val filteredList = filterHeros.execute(
            current = state.value.heros,
            heroFilter = state.value.heroFilter,
            attributeFilter = state.value.filterPrimaryAttribute,
            heroName = state.value.filterQuery
        ).toMutableList()
        state.value = state.value.copy(
            filteredList = filteredList
        )
    }

    private fun updateFilterQuery(query: String) {
        state.value = state.value.copy(
            filterQuery = query
        )
    }

    private fun getHeros() {
        getHeros.execute().onEach {
            when (it) {
                is DataState.Response -> {
                    if (it.uiComponent is UIComponent.Dialog) {
                        dispatchMessage(it.uiComponent)
                    } else {
                        logger.log((it.uiComponent as UIComponent.None).message)
                    }
                }
                is DataState.Data -> {
                    state.value = state.value.copy(
                        heros = it.data ?: listOf(),
                        progressBarState = ProgressBarState.Idle
                    )
                    filterHeros()
                }
                is DataState.Loading -> {
                    state.value = state.value.copy(
                        progressBarState = it.state,
                    )
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
