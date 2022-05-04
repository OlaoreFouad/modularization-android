package dev.olaore.dotainfo.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.navArgument

sealed class Screen(
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {

    object HeroList: Screen("heroList")

    object HeroDetail: Screen(
        "heroDetail",
        arguments = listOf(
            navArgument("heroId") {
                type = NavType.IntType
            }
        )
    )

}