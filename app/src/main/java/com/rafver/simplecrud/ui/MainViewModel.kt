package com.rafver.simplecrud.ui

import com.rafver.core_ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): BaseViewModel<MainUiState, MainViewEvent, MainViewModelEffect>(
    MainUiState()
) {
    override suspend fun handleViewEvent(event: MainViewEvent) {
        when(event) {
            is MainViewEvent.OnBottomNavigationItemClicked -> {
                updateState(currentState.copy(currentSelectedNavigationItemIndex = event.itemIndex))
                onViewModelEffect(MainViewModelEffect.NavigateTo(event.route))
            }
        }
    }
}