package dev.olaore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.olaore.core.domain.ProgressBarState
import dev.olaore.core.domain.ProgressBarState.*
import dev.olaore.core.domain.Queue
import dev.olaore.core.domain.UIComponent

@Composable
fun DefaultScreen(
    progressBarState: ProgressBarState = Idle,
    queue: Queue<UIComponent> = Queue(mutableListOf()),
    onRemoveHeadFromQueue: () -> Unit,
    content: @Composable () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            content()

            if (!queue.isEmpty()) {
                queue.peek()?.let { uiComponent ->
                    if (uiComponent is UIComponent.Dialog) {
                        GenericDialog(
                            title = uiComponent.title,
                            description = uiComponent.description,
                            modifier = Modifier.fillMaxWidth(.9f),
                            onRemoveHeadFromQueue = onRemoveHeadFromQueue
                        )
                    }
                }
            }

            if (progressBarState is Loading) {
                CircularIndeterminateProgressBar()
            }
        }
    }
}
