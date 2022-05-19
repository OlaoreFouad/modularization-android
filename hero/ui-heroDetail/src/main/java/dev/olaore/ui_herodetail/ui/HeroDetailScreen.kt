package dev.olaore.ui_herodetail.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import coil.ImageLoader
import dev.olaore.components.DefaultScreen
import dev.olaore.ui_herodetail.ui.state.HeroDetailEvents
import dev.olaore.ui_herodetail.ui.state.HeroDetailEvents.*
import dev.olaore.ui_herodetail.ui.state.HeroDetailState

@Composable
fun HeroDetailScreen(
    state: HeroDetailState,
    imageLoader: ImageLoader,
    propagateEvent: (HeroDetailEvents) -> Unit
) {
    DefaultScreen(
        progressBarState = state.progressBarState,
        queue = state.errorQueue,
        onRemoveHeadFromQueue = {
            propagateEvent(RemoveMessageFromQueue)
        }
    ) {
        state.hero?.let {
            HeroDetail(state.hero, imageLoader)
        } ?: Text(text = "Loading....")
    }
}
