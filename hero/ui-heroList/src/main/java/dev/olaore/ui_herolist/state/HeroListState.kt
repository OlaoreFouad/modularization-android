package dev.olaore.ui_herolist.state

import dev.olaore.core.domain.ProgressBarState
import dev.olaore.hero_domain.Hero

data class HeroListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val heros: List<Hero> = listOf(),
    val filterQuery: String = "",
    val filteredList: MutableList<Hero> = mutableListOf()
)