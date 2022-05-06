package dev.olaore.dotainfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import dev.olaore.dotainfo.ui.navigation.Screen
import dev.olaore.dotainfo.ui.theme.DotaInfoTheme
import dev.olaore.ui_herodetail.HeroDetailViewModel
import dev.olaore.ui_herodetail.ui.HeroDetailScreen
import dev.olaore.ui_herolist.HeroListScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DotaInfoTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.HeroList.route
                ) {
                    addHeroListToGraph(navController, imageLoader)
                    addHeroDetailToGraph(imageLoader)
                }
            }
        }
    }
}

fun NavGraphBuilder.addHeroListToGraph(
    navController: NavController,
    imageLoader: ImageLoader
) {
    composable(Screen.HeroList.route) {
        val viewModel: HeroListViewModel = hiltViewModel()
        HeroListScreen(state = viewModel.state.value, imageLoader) { heroId ->
            navController.navigate(Screen.HeroDetail.route + "/$heroId")
        }
    }
}

fun NavGraphBuilder.addHeroDetailToGraph(
    imageLoader: ImageLoader
) {
    composable(
        Screen.HeroDetail.route + "/{heroId}"
    ) {
        val viewModel: HeroDetailViewModel = hiltViewModel()
        HeroDetailScreen(viewModel.state.value, imageLoader)
    }
}














