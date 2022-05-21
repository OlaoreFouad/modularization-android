package dev.olaore.ui_heroDetail.ui

import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import dev.olaore.hero_datasource_test.network.data.HeroDataValid
import dev.olaore.hero_datasource_test.network.serializeHeroData
import dev.olaore.ui_heroDetail.coil.FakeImageLoader
import dev.olaore.ui_herodetail.ui.HeroDetailScreen
import dev.olaore.ui_herodetail.ui.state.HeroDetailState
import kotlin.random.Random
import org.junit.Rule
import org.junit.Test

class HeroDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader.build(context)
    private val heros = serializeHeroData(HeroDataValid.data)

    @Test
    fun given_heroWithRequiredProperties_shouldDisplayDetailsOnScreen() {
        val selectedHero = heros[Random.nextInt(0, heros.size - 1)]
        composeTestRule.setContent {
            val state = remember { HeroDetailState(hero = selectedHero) }
            HeroDetailScreen(
                state = state,
                propagateEvent = {},
                imageLoader = imageLoader
            )
        }

        composeTestRule.apply {
            onNodeWithText(selectedHero.localizedName).assertIsDisplayed()
            onNodeWithText(selectedHero.primaryAttribute.value).assertIsDisplayed()
            onNodeWithText(selectedHero.attackType.uiValue).assertIsDisplayed()
        }
    }

}
