package dev.olaore.ui_herodetail.ui.state

sealed class HeroDetailEvents {
    data class GetHero(
        val id: Int
    ): HeroDetailEvents()

    object RemoveMessageFromQueue: HeroDetailEvents()
}
