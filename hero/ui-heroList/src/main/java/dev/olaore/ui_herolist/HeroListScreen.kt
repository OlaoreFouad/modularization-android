package dev.olaore.ui_herolist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.olaore.core.domain.ProgressBarState
import dev.olaore.ui_herolist.state.HeroListState

@Composable
fun HeroListScreen(
    state: HeroListState
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (state.progressBarState) {
            is ProgressBarState.Idle -> {
                HeroList(heros = state.heros)
            }
            is ProgressBarState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}