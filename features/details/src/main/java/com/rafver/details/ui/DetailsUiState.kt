package com.rafver.details.ui

import androidx.annotation.StringRes
import com.rafver.core_ui.models.UserUiModel
import com.rafver.core_ui.viewmodel.UiState
import com.rafver.core_ui.viewmodel.ViewEvent
import com.rafver.core_ui.viewmodel.ViewModelEffect

data class DetailsUiState(
    val userModel: UserUiModel? = null,
    val loading: Boolean = false
) : UiState

sealed class DetailsViewEvent: ViewEvent {
    data object OnEditClicked: DetailsViewEvent()
    data object OnDeleteClicked: DetailsViewEvent()
}

sealed class DetailsViewModelEffect: ViewModelEffect {
    data class DisplaySnackbar(@StringRes val resId: Int): DetailsViewModelEffect()
}