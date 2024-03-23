package com.rafver.core_ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModel<State: UiState, Event: ViewEvent>(initialUiState: State): ViewModel() {

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: Flow<State> = _uiState.onEach {
        println("Emitted new State: $it")
    }

    val currentState get() = _uiState.value

    protected fun updateState(newState: State) {
        _uiState.value = newState
    }

    protected abstract fun onViewEvent(newEvent: Event)
}