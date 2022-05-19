package dev.olaore.dotainfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import coil.ImageLoader
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.olaore.dotainfo.ui.navigation.Screen
import dev.olaore.dotainfo.ui.theme.DotaInfoTheme
import dev.olaore.ui_herodetail.HeroDetailViewModel
import dev.olaore.ui_herodetail.ui.HeroDetailScreen
import dev.olaore.ui_herolist.HeroListScreen
import javax.inject.Inject

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalComposeUiApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DotaInfoTheme {
                val animatedNavController = rememberAnimatedNavController()
                AnimatedNavHost(
                    navController = animatedNavController,
                    startDestination = Screen.HeroList.route
                ) {
                    addHeroListToGraph(animatedNavController, imageLoader)
                    addHeroDetailToGraph(imageLoader)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalComposeUiApi
fun NavGraphBuilder.addHeroListToGraph(
    navController: NavController,
    imageLoader: ImageLoader
) {
    composable(
        Screen.HeroList.route,
        exitTransition = { _, _ ->
            slideOutHorizontally(
                targetOffsetX = { -300 },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { -300 },
                animationSpec = tween(300)
            ) + fadeIn(animationSpec = tween(durationMillis = 300))
        }
    ) {
        val viewModel: HeroListViewModel = hiltViewModel()
        HeroListScreen(
            state = viewModel.state.value,
            imageLoader,
            propagateEvent = viewModel::onTriggerEvent
        ) { heroId ->
            navController.navigate(Screen.HeroDetail.route + "/$heroId")
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addHeroDetailToGraph(
    imageLoader: ImageLoader
) {
    composable(
        Screen.HeroDetail.route + "/{heroId}",
        enterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { 300 },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = { _, _ ->
            slideOutHorizontally(
                targetOffsetX = { 300 },
                animationSpec = tween(300)
            ) + fadeOut(animationSpec = tween(durationMillis = 300))
        }
    ) {
        val viewModel: HeroDetailViewModel = hiltViewModel()
        HeroDetailScreen(
            viewModel.state.value,
            imageLoader,
            propagateEvent = viewModel::onTriggerEvent
        )
    }
}














