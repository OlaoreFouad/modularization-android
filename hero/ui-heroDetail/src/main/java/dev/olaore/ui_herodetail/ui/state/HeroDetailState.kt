package dev.olaore.ui_herodetail.ui.state

import dev.olaore.core.domain.ProgressBarState
import dev.olaore.hero_domain.Hero

data class HeroDetailState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val hero: Hero? = null
)