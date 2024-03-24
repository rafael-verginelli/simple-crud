package com.rafver.core_ui.viewmodel

import androidx.lifecycle.ViewModel
import com.rafver.core_ui.util.SingleEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach

abstract class BaseViewModel<State: UiState, Event: ViewEvent, Effect: ViewModelEffect>(
    initialUiState: State
): ViewModel() {

    private val _snackbarEvent = MutableStateFlow<SingleEvent<String>?>(null)
    val snackbarEvent = _snackbarEvent.asStateFlow()

    protected fun onSnackbarEvent(message: String) {
        _snackbarEvent.value = SingleEvent(message)
    }

    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: Flow<State> = _uiState.onEach {
        println("Emitted new State: $it")
    }

    val currentState get() = _uiState.value

    protected fun updateState(newState: State) {
        _uiState.value = newState
    }

    protected abstract fun onViewEvent(newEvent: Event)
    protected abstract fun onViewModelEffect(newEffect: Effect)
}