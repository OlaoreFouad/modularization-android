package dev.olaore.ui_heroList.ui

import android.content.Context
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import coil.ImageLoader
import dev.olaore.hero_datasource_test.network.data.HeroDataValid
import dev.olaore.hero_datasource_test.network.serializeHeroData
import dev.olaore.ui_heroList.coil.FakeImageLoader
import dev.olaore.ui_herolist.HeroListScreen
import dev.olaore.ui_herolist.state.HeroListState
import org.junit.Rule
import org.junit.Test

class HeroListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()
    
    private val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
    private val imageLoader: ImageLoader = FakeImageLoader.build(context)
    private val heros = serializeHeroData(HeroDataValid.data)
    
    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun given_stateWithHerosIsProvided_should_displayCorrectHerosOnScreen() {
        
        composeRule.setContent { 
            val state = remember {
                HeroListState(
                    heros = heros,
                    filteredList = heros.toMutableList()
                )
            }
            HeroListScreen(
                state = state,
                imageLoader = imageLoader,
                propagateEvent = {},
                onItemClicked = {}
            )
        }

        composeRule.onNodeWithText("Anti-Mage").assertExists()
        composeRule.onNodeWithText("Axe").assertExists()
        composeRule.onNodeWithText("Bane").assertExists()

    }

}
