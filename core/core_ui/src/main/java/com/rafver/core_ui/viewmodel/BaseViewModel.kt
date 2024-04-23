package com.rafver.core_ui.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafver.core_ui.util.SingleEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State: UiState, Event: ViewEvent, Effect: ViewModelEffect>(
    initialUiState: State
): ViewModel() {

    // State
    private val _uiState = MutableStateFlow(initialUiState)
    val uiState: Flow<State> = _uiState.onEach {
        println("Emitted new State: $it")
    }

    val currentState get() = _uiState.value

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    fun updateState(newState: State) {
        _uiState.value = newState
    }
    // -------

    // Event
    private val _events = MutableSharedFlow<SingleEvent<Event>>()

    fun onViewEvent(newEvent: Event) {
        viewModelScope.launch {
            _events.emit(SingleEvent(newEvent))
        }
    }

    protected abstract suspend fun handleViewEvent(event: Event)

    init {
        viewModelScope.launch {
            _events.collect { event ->
                event.handleSingleEvent()?.let {
                    println("Emitted new Event: $it")
                    handleViewEvent(it)
                }
            }
        }
    }
    // -------

    // Effect
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val effectsChannel = Channel<Effect>()

    val effects = effectsChannel.receiveAsFlow().onEach {
        println("Emitted new Effect: $it")
    }


    protected fun onViewModelEffect(effect: Effect) {
        viewModelScope.launch {
            effectsChannel.send(effect)
        }
    }
    // -------

    // Error Handling
    abstract fun handleException(error: Throwable?)
}
