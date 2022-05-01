package dev.olaore.ui_herolist

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import dev.olaore.hero_domain.Hero
import dev.olaore.ui_herolist.components.HeroListItem
import dev.olaore.ui_herolist.state.HeroListState

@Composable
fun HeroList(
    heros: List<Hero>
) {
    LazyColumn {
        items(heros) {
            HeroListItem(hero = it) {
            }
        }
    }
}
