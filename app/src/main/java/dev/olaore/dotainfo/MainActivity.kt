package dev.olaore.dotainfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import coil.ImageLoader
import dagger.hilt.android.AndroidEntryPoint
import dev.olaore.dotainfo.ui.theme.DotaInfoTheme
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
                val viewModel: HeroListViewModel by viewModels()
                HeroListScreen(state = viewModel.state.value, imageLoader)
            }
        }
    }
}















