package com.rafver.details.ui

import androidx.annotation.StringRes
import com.rafver.core_ui.models.UserUiModel
import com.rafver.core_ui.viewmodel.UiState
import com.rafver.core_ui.viewmodel.ViewEvent
import com.rafver.core_ui.viewmodel.ViewModelEffect

data class DetailsUiState(
    val userModel: UserUiModel? = null,
    val loading: Boolean = false,
    val showDeleteDialog: Boolean = false,
) : UiState

sealed class DetailsViewEvent: ViewEvent {
    data object OnInitialize: DetailsViewEvent()
    data object OnEditClicked: DetailsViewEvent()
    data object OnDeleteClicked: DetailsViewEvent()
    data object OnDeleteConfirmationClicked: DetailsViewEvent()
    data object OnDeleteCancelClicked: DetailsViewEvent()
}

sealed class DetailsViewModelEffect: ViewModelEffect {
    data class DisplaySnackbar(@StringRes val resId: Int): DetailsViewModelEffect()
    data class NavigateToEdit(val userId: String): DetailsViewModelEffect()
    data object NavigateUp: DetailsViewModelEffect()
}