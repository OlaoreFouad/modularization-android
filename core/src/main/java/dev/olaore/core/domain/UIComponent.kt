package dev.olaore.core.domain

sealed class UIComponent {

    data class Dialog(
        val title: String,
        val description: String
    ): UIComponent()

    object None: UIComponent()

}