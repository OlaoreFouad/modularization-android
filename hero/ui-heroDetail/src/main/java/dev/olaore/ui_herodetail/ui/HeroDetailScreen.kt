package dev.olaore.ui_herodetail.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import coil.ImageLoader
import dev.olaore.ui_herodetail.ui.state.HeroDetailState

@Composable
fun HeroDetailScreen(
    state: HeroDetailState,
    imageLoader: ImageLoader
) {
    state.hero?.let {
        HeroDetail(state.hero, imageLoader)
    } ?: Text(text = "Loading....")
}