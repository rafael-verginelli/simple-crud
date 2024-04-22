package com.rafver.create.ui.models

import androidx.annotation.StringRes
import com.rafver.core_ui.viewmodel.UiState
import com.rafver.core_ui.viewmodel.ViewEvent
import com.rafver.core_ui.viewmodel.ViewModelEffect

data class CreateUiState(
    val name: String = "",
    val age: String = "",
    val email: String = "",
    val loading: Boolean = false,
    val errors: CreateUiErrorState = CreateUiErrorState(),
    val isEditMode: Boolean = false,
): UiState

data class CreateUiErrorState(
    @StringRes val mandatoryNameError: Int? = null,
    @StringRes val mandatoryAgeError: Int? = null,
    @StringRes val invalidAgeError: Int? = null,
    @StringRes val mandatoryEmailError: Int? = null,
)

sealed class CreateViewEvent: ViewEvent {
    data object OnInitialize: CreateViewEvent()
    data class OnNameChanged(val newValue: String): CreateViewEvent()
    data class OnAgeChanged(val newValue: String): CreateViewEvent()
    data class OnEmailChanged(val newValue: String): CreateViewEvent()
    data object OnDiscardClicked: CreateViewEvent()
    data object OnCreateClicked: CreateViewEvent()
    data object OnUpdateClicked: CreateViewEvent()
}

sealed class CreateViewModelEffect: ViewModelEffect {
    data class DisplaySnackbar(@StringRes val resId: Int): CreateViewModelEffect()
    data object OnNameTextInputFocusRequest: CreateViewModelEffect()
}