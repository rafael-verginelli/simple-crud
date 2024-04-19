package com.rafver.read.ui.models

import androidx.annotation.StringRes
import com.rafver.core_ui.models.UserUiModel
import com.rafver.core_ui.viewmodel.UiState
import com.rafver.core_ui.viewmodel.ViewEvent
import com.rafver.core_ui.viewmodel.ViewModelEffect

data class ReadUiState(
    val userList: List<UserUiModel>? = null,
    val loading: Boolean = false,
): UiState

sealed class ReadViewEvent: ViewEvent {
    data object OnInitialize: ReadViewEvent()
    data class OnListItemClicked(val userId: String): ReadViewEvent()
}

sealed class ReadViewModelEffect: ViewModelEffect {
    data class DisplaySnackbar(@StringRes val resId: Int): ReadViewModelEffect()
    data class NavigateToDetails(val userId: String): ReadViewModelEffect()
}