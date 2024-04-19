package com.rafver.core_ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.rafver.core_ui.viewmodel.BaseViewModel
import com.rafver.core_ui.viewmodel.UiState
import com.rafver.core_ui.viewmodel.ViewEvent
import com.rafver.core_ui.viewmodel.ViewModelEffect
import androidx.compose.runtime.State as ComposeState

/**
 * Method that observes [UiState] and triggers Recomposition every time the value is updated.
 */
@Composable
fun <State: UiState, Event: ViewEvent, Effect: ViewModelEffect>BaseViewModel<State, Event, Effect>
        .collectUiState(): ComposeState<State> {
    return uiState.collectAsState(initial = currentState)
}