package dev.olaore.ui_herolist.state

import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.UIComponentState
import dev.olaore.hero_domain.Hero
import dev.olaore.hero_domain.HeroAttribute
import dev.olaore.hero_domain.HeroFilter

data class HeroListState(
    val progressBarState: ProgressBarState = ProgressBarState.Idle,
    val heros: List<Hero> = listOf(),
    val filterQuery: String = "",
    val filteredList: MutableList<Hero> = mutableListOf(),
    val heroFilter: HeroFilter = HeroFilter.Hero(),
    val filterPrimaryAttribute: HeroAttribute = HeroAttribute.Unknown,
    val filterDialogState: UIComponentState = UIComponentState.Hide
)
