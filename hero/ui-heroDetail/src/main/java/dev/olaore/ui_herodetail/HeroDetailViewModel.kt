package dev.olaore.ui_herodetail

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.olaore.hero_interactors.usecases.GetHeroUseCase
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val getHero: GetHeroUseCase
) {
}