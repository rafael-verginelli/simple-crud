package com.rafver.create.ui.models

import androidx.annotation.StringRes
import com.rafver.core_ui.viewmodel.UiState
import com.rafver.core_ui.viewmodel.ViewEvent
import com.rafver.core_ui.viewmodel.ViewModelEffect
import com.rafver.create.data.CreateResultType

data class CreateUiState(
    val name: String = "",
    val age: String = "",
    val email: String = "",
    val loading: Boolean = false,
    val errors: CreateUiErrorState = CreateUiErrorState(),
): UiState

data class CreateUiErrorState(
    val mandatoryNameError: CreateResultType.Error.NameMandatory? = null,
    val mandatoryAgeError: CreateResultType.Error.AgeMandatory? = null,
    val invalidAgeError: CreateResultType.Error.InvalidAge? = null,
    val mandatoryEmailError: CreateResultType.Error.EmailMandatory? = null,
)

sealed class CreateViewEvent: ViewEvent {
    data class OnNameChanged(val newValue: String): CreateViewEvent()
    data class OnAgeChanged(val newValue: String): CreateViewEvent()
    data class OnEmailChanged(val newValue: String): CreateViewEvent()
    data object OnDiscardClicked: CreateViewEvent()
    data object OnCreateClicked: CreateViewEvent()
}

sealed class CreateViewModelEffect: ViewModelEffect {
    data class DisplaySnackbar(@StringRes val resId: Int): CreateViewModelEffect()
    data object OnNameTextInputFocusRequest: CreateViewModelEffect()
}