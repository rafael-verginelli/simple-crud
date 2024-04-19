package com.rafver.simplecrud.ui

import com.rafver.core_ui.viewmodel.UiState
import com.rafver.core_ui.viewmodel.ViewEvent
import com.rafver.core_ui.viewmodel.ViewModelEffect

data class MainUiState(
    val currentSelectedNavigationItemIndex: Int = 0,
): UiState

sealed class MainViewEvent: ViewEvent {
    data class OnBottomNavigationItemClicked(
        val route: String,
        val itemIndex: Int,
    ): MainViewEvent()
}

sealed class MainViewModelEffect: ViewModelEffect {
    data class NavigateTo(val route: String): MainViewModelEffect()
}