package dev.olaore.ui_herolist.state

sealed class HeroListEvents {
    object GetHeros: HeroListEvents()
    object FilterHeros: HeroListEvents()
    data class UpdateFilterQuery(
        val query: String
    ): HeroListEvents()
}