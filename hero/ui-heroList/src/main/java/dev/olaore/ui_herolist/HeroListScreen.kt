package dev.olaore.ui_herolist

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import coil.ImageLoader
import dev.olaore.components.DefaultScreen
import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.UIComponentState
import dev.olaore.core.domain.UIComponentState.*
import dev.olaore.ui_herolist.components.HeroListFilter
import dev.olaore.ui_herolist.components.HeroListToolbar
import dev.olaore.ui_herolist.state.HeroListEvents
import dev.olaore.ui_herolist.state.HeroListEvents.*
import dev.olaore.ui_herolist.state.HeroListState

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalComposeUiApi
@Composable
fun HeroListScreen(
    state: HeroListState,
    imageLoader: ImageLoader,
    propagateEvent: (HeroListEvents) -> Unit,
    onItemClicked: (Int) -> Unit,
) {
    DefaultScreen(
        progressBarState = state.progressBarState,
        queue = state.errorQueue,
        onRemoveHeadFromQueue = {
            propagateEvent(RemoveMessageFromQueue)
        }
    ) {
        Column {
            val name = remember {
                mutableStateOf("")
            }
            HeroListToolbar(
                heroName = state.filterQuery,
                onHeroNameChanged = {
                    propagateEvent(UpdateFilterQuery(it))
                },
                onExecuteSearch = {
                    propagateEvent(FilterHeros)
                },
                onShowFilterDialog = {
                    propagateEvent(UpdateFilterDialogState(Show))
                }
            )
            Box(modifier = Modifier.fillMaxSize()) {
                HeroList(
                    heros = state.filteredList,
                    imageLoader
                ) { heroId ->
                    onItemClicked.invoke(heroId)
                }
                if (state.filterDialogState is Show) {
                    HeroListFilter(
                        heroFilter = state.heroFilter,
                        onUpdateHeroFilter = { heroFilter ->
                            propagateEvent(UpdateHeroFilter(heroFilter))
                        },
                        attributeFilter = state.filterPrimaryAttribute,
                        onUpdateAttributeFilter = {
                            propagateEvent(UpdateHeroAttributeFilter(it))
                        },
                        onCloseDialog = {
                            propagateEvent(UpdateFilterDialogState(Hide))
                        }
                    )
                }
            }
        }
    }
}
