package com.rafver.create.ui.models

import com.rafver.core_ui.viewmodel.UiState
import com.rafver.core_ui.viewmodel.ViewEvent

data class CreateUiState(
    val name: String = "",
    val age: String = "",
    val email: String = "",
    val loading: Boolean = false,
): UiState

sealed class CreateViewEvent: ViewEvent {
    data class OnNameChanged(val newValue: String): CreateViewEvent()
    data class OnAgeChanged(val newValue: String): CreateViewEvent()
    data class OnEmailChanged(val newValue: String): CreateViewEvent()
    data object OnDiscardClicked: CreateViewEvent()
    data object OnUpdateClicked: CreateViewEvent()
}