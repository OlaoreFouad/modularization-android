package dev.olaore.dotainfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dev.olaore.core.domain.DataState.*
import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.UIComponent
import dev.olaore.core.util.Logger
import dev.olaore.dotainfo.ui.theme.DotaInfoTheme
import dev.olaore.hero_domain.Hero
import dev.olaore.hero_interactors.interactors.HeroInteractors
import dev.olaore.ui_herolist.HeroListScreen
import dev.olaore.ui_herolist.state.HeroListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    private val state = mutableStateOf(HeroListState())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val heroInteractors = HeroInteractors.build(
            sqlDriver = AndroidSqliteDriver(
                schema = HeroInteractors.schema,
                name = HeroInteractors.dbName,
                context = this,
            )
        )
        val logger = Logger("MainActivity")
        heroInteractors.getHeros.execute().onEach {
            when (it) {
                is Response -> {
                    if (it.uiComponent is UIComponent.Dialog) {
                        logger.log((it.uiComponent as UIComponent.Dialog).description)
                    } else {
                        logger.log((it.uiComponent as UIComponent.None).message)
                    }
                }
                is Data -> {
                    state.value = state.value.copy(heros = it.data ?: listOf())
                }
                is Loading -> {
                    state.value = state.value.copy(
                        progressBarState = ProgressBarState.Loading,
                    )
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))

        setContent {
            DotaInfoTheme {
                HeroListScreen(state = state.value)
            }
        }
    }
}















