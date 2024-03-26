package com.rafver.create.ui

import com.rafver.core_ui.viewmodel.BaseViewModel
import com.rafver.create.R
import com.rafver.create.ui.models.CreateUiState
import com.rafver.create.ui.models.CreateViewEvent
import com.rafver.create.ui.models.CreateViewModelEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor()
    : BaseViewModel<CreateUiState, CreateViewEvent, CreateViewModelEffect>(CreateUiState())
{
        // ToDo: consider merging Snackbar Event and ViewEvent. ViewModelEffect triggers the Launched Effect in the Composable, which clears TextInput focus and calls ViewEvent.
    override suspend fun handleViewEvent(event: CreateViewEvent) {
        when(event) {
            CreateViewEvent.OnDiscardClicked -> {
                clearForm()
                onViewModelEffect(CreateViewModelEffect.OnNameTextInputFocusRequest)
                onSnackbarEvent(R.string.snackbar_msg_changes_discarded)
            }
            CreateViewEvent.OnCreateClicked -> {
                clearForm()
                onSnackbarEvent(R.string.snackbar_msg_user_created)
            }
            is CreateViewEvent.OnAgeChanged -> {
                updateState(currentState.copy(age = event.newValue))
            }
            is CreateViewEvent.OnEmailChanged -> {
                updateState(currentState.copy(email = event.newValue))
            }
            is CreateViewEvent.OnNameChanged -> {
                updateState(currentState.copy(name = event.newValue))
            }
        }
    }

    private fun clearForm() {
        updateState(currentState.copy(name = "", age = "", email = ""))
    }
}