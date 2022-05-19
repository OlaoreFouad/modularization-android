package dev.olaore.ui_herolist.state

import dev.olaore.core.domain.UIComponentState
import dev.olaore.hero_domain.HeroAttribute
import dev.olaore.hero_domain.HeroFilter

sealed class HeroListEvents {
    object GetHeros: HeroListEvents()

    object FilterHeros: HeroListEvents()

    data class UpdateFilterQuery(
        val query: String
    ): HeroListEvents()

    data class UpdateHeroFilter(
        val heroFilter: HeroFilter
    ): HeroListEvents()

    data class UpdateFilterDialogState(
        val dialogState: UIComponentState
    ): HeroListEvents()

    data class UpdateHeroAttributeFilter(
        val attribute: HeroAttribute
    ): HeroListEvents()
}
