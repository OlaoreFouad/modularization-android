package dev.olaore.dotainfo.ui

import android.content.Context
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertAny
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import dev.olaore.core.util.Logger
import dev.olaore.dotainfo.HeroListViewModel
import dev.olaore.dotainfo.MainActivity
import dev.olaore.dotainfo.coil.FakeImageLoader
import dev.olaore.dotainfo.di.AppModule
import dev.olaore.dotainfo.ui.navigation.Screen
import dev.olaore.dotainfo.ui.theme.DotaInfoTheme
import dev.olaore.hero_datasource.cache.HeroCache
import dev.olaore.hero_datasource.network.HeroService
import dev.olaore.hero_datasource_test.cache.FakeHeroCache
import dev.olaore.hero_datasource_test.cache.FakeHeroDatabase
import dev.olaore.hero_datasource_test.network.FakeHeroService
import dev.olaore.hero_datasource_test.network.HeroServiceResponseType
import dev.olaore.hero_domain.HeroAttribute
import dev.olaore.hero_interactors.interactors.HeroInteractors
import dev.olaore.hero_interactors.usecases.FilterHerosUseCase
import dev.olaore.hero_interactors.usecases.GetHeroUseCase
import dev.olaore.hero_interactors.usecases.GetHerosUseCase
import dev.olaore.ui_herodetail.HeroDetailViewModel
import dev.olaore.ui_herodetail.ui.HeroDetailScreen
import dev.olaore.ui_herolist.HeroListScreen
import dev.olaore.ui_herolist.ui.test.TAG_HERO_FILTER_AGILITY_CHECKBOX
import dev.olaore.ui_herolist.ui.test.TAG_HERO_FILTER_ASC
import dev.olaore.ui_herolist.ui.test.TAG_HERO_FILTER_BTN
import dev.olaore.ui_herolist.ui.test.TAG_HERO_FILTER_DESC
import dev.olaore.ui_herolist.ui.test.TAG_HERO_FILTER_DIALOG
import dev.olaore.ui_herolist.ui.test.TAG_HERO_FILTER_DIALOG_DONE
import dev.olaore.ui_herolist.ui.test.TAG_HERO_FILTER_HERO_CHECKBOX
import dev.olaore.ui_herolist.ui.test.TAG_HERO_FILTER_INT_CHECKBOX
import dev.olaore.ui_herolist.ui.test.TAG_HERO_FILTER_PROWINS_CHECKBOX
import dev.olaore.ui_herolist.ui.test.TAG_HERO_FILTER_STENGTH_CHECKBOX
import dev.olaore.ui_herolist.ui.test.TAG_HERO_NAME
import dev.olaore.ui_herolist.ui.test.TAG_HERO_PRIMARY_ATTRIBUTE
import dev.olaore.ui_herolist.ui.test.TAG_HERO_SEARCH_BAR
import javax.inject.Singleton
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalComposeUiApi::class)
@UninstallModules(AppModule::class)
@HiltAndroidTest
class HeroListEndToEndTest {

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModuleTest {

        @Provides
        @Singleton
        fun provideHeroCache(): HeroCache {
            return FakeHeroCache(FakeHeroDatabase())
        }

        @Provides
        @Singleton
        fun provideHeroService(): HeroService {
            return FakeHeroService.build(
                type = HeroServiceResponseType.GoodData
            )
        }

        @Provides
        @Singleton
        fun provideHeroInteractors(
            service: HeroService,
            cache: HeroCache
        ): HeroInteractors {
            val logger = Logger("")
            return HeroInteractors(
                getHeros = GetHerosUseCase(service, cache, logger),
                getHero = GetHeroUseCase(cache),
                filterHeros = FilterHerosUseCase()
            )
        }

        @Provides
        @Singleton
        fun provideImageLoader(@ApplicationContext ctx: Context): ImageLoader {
            return FakeImageLoader.build(ctx)
        }

    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader.build(context)

    @Before
    fun before(){
        composeTestRule.setContent {
            DotaInfoTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.HeroList.route,
                    builder = {
                        composable(
                            route = Screen.HeroList.route,
                        ){
                            val viewModel: HeroListViewModel = hiltViewModel()
                            HeroListScreen(
                                state = viewModel.state.value,
                                propagateEvent = viewModel::onTriggerEvent,
                                onItemClicked = { heroId ->
                                    navController.navigate("${Screen.HeroDetail.route}/$heroId")
                                },
                                imageLoader = imageLoader,
                            )
                        }
                        composable(
                            route = Screen.HeroDetail.route + "/{heroId}",
                            arguments = Screen.HeroDetail.arguments,
                        ){
                            val viewModel: HeroDetailViewModel = hiltViewModel()
                            HeroDetailScreen(
                                state = viewModel.state.value,
                                propagateEvent = viewModel::onTriggerEvent,
                                imageLoader = imageLoader
                            )
                        }
                    }
                )
            }
        }
    }

    @Test
    fun testSearchHeroByName(){
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("TAG") // For learning the ui tree system

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Anti-Mage")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Anti-Mage",
        )
        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextClearance()

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Storm Spirit")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Storm Spirit",
        )
        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextClearance()

        composeTestRule.onNodeWithTag(TAG_HERO_SEARCH_BAR).performTextInput("Mirana")
        composeTestRule.onNodeWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertTextEquals(
            "Mirana",
        )
    }

    @Test
    fun testFilterHeroAlphabetically(){
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        // Filter by "Hero" name
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_HERO_CHECKBOX).performClick()

        // Order Descending (z-a)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DESC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertAny(hasText("Zeus"))

        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Order Ascending (a-z)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_ASC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertAny(hasText("Abaddon"))
    }

    @Test
    fun testFilterHeroByProWins(){
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        // Filter by ProWin %
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_PROWINS_CHECKBOX).performClick()

        // Order Descending (100% - 0%)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DESC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertAny(hasText("Chen"))

        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Order Ascending (0% - 100%)
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_ASC).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm the order is correct
        composeTestRule.onAllNodesWithTag(TAG_HERO_NAME, useUnmergedTree = true).assertAny(hasText("Dawnbreaker"))
    }

    @Test
    fun testFilterHeroByStrength(){
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_STENGTH_CHECKBOX).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm that only STRENGTH heros are showing
        composeTestRule.onAllNodesWithTag(TAG_HERO_PRIMARY_ATTRIBUTE, useUnmergedTree = true).assertAll(hasText(HeroAttribute.Strength.value))
    }

    @Test
    fun testFilterHeroByAgility(){
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_AGILITY_CHECKBOX).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm that only STRENGTH heros are showing
        composeTestRule.onAllNodesWithTag(TAG_HERO_PRIMARY_ATTRIBUTE, useUnmergedTree = true).assertAll(hasText(HeroAttribute.Agility.value))
    }

    @Test
    fun testFilterHeroByIntelligence(){
        // Show the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_BTN).performClick()

        // Confirm the filter dialog is showing
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG).assertIsDisplayed()

        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_INT_CHECKBOX).performClick()

        // Close the dialog
        composeTestRule.onNodeWithTag(TAG_HERO_FILTER_DIALOG_DONE).performClick()

        // Confirm that only STRENGTH heros are showing
        composeTestRule.onAllNodesWithTag(TAG_HERO_PRIMARY_ATTRIBUTE, useUnmergedTree = true).assertAll(hasText(
            HeroAttribute.Intelligence.value))
    }

}
