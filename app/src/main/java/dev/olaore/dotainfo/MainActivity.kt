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
import dev.olaore.core.domain.DataState.*
import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.UIComponent
import dev.olaore.core.util.Logger
import dev.olaore.dotainfo.ui.theme.DotaInfoTheme
import dev.olaore.hero_domain.Hero
import dev.olaore.hero_interactors.interactors.HeroInteractors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainActivity : ComponentActivity() {

    private val heros = mutableStateOf<List<Hero>>(listOf())
    private val progressBarState = mutableStateOf<ProgressBarState>(
        ProgressBarState.Idle
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val heroInteractors = HeroInteractors.build()
        val logger = Logger("MainActivity")
        heroInteractors.getHeros.execute().onEach {
            when (it) {
                is Response -> {
                    if (it.uiComponent is UIComponent.Dialog) {
                        logger.log((it.uiComponent as UIComponent.Dialog).title)
                    } else {
                        logger.log((it.uiComponent as UIComponent.None).message)
                    }
                }
                is Data -> {
                    heros.value = it.data ?: listOf()
                }
                is Loading -> {
                    progressBarState.value = it.state
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))

        setContent {
            DotaInfoTheme {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)) {

                    LazyColumn {
                        items(heros.value) {
                            Text(text = it.localizedName)
                        }
                    }

                    if (progressBarState.value is ProgressBarState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                }
            }
        }
    }
}















