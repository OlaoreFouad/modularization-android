package dev.olaore.ui_herodetail.ui.state

import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.Queue
import dev.olaore.core.domain.UIComponent
import dev.olaore.hero_domain.Hero

data class HeroDetailState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val hero: Hero? = null,
    val errorQueue: Queue<UIComponent> = Queue(mutableListOf())
)
