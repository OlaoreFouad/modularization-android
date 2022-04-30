package dev.olaore.core.domain

sealed class DataState<out T> {
    data class Response(
        val uiComponent: UIComponent
    ): DataState<Nothing>()

    data class Data<T>(
        val data: T? = null
    ): DataState<T>()

    data class Loading(
        val state: ProgressBarState = ProgressBarState.Idle
    ): DataState<Nothing>()

}