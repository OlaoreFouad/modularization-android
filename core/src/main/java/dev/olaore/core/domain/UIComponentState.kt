package dev.olaore.core.domain

sealed class UIComponentState {

    object Hide: UIComponentState()

    object Show: UIComponentState()

}
