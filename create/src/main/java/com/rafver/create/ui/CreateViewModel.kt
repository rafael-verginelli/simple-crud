package com.rafver.create.ui

import com.rafver.core_ui.viewmodel.BaseViewModel
import com.rafver.create.ui.models.CreateUiState
import com.rafver.create.ui.models.CreateViewEvent
import com.rafver.create.ui.models.CreateViewModelEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor()
    : BaseViewModel<CreateUiState, CreateViewEvent, CreateViewModelEffect>(CreateUiState())
{
    public override fun onViewEvent(newEvent: CreateViewEvent) {
        when(newEvent) {
            CreateViewEvent.OnDiscardClicked -> {
                clearForm()
                onViewModelEffect(CreateViewModelEffect.ShowDiscardSnackbar)
            }
            CreateViewEvent.OnUpdateClicked -> {
                println(
                    "Creating User with name = " +
                            "${currentState.name}, " +
                            "age = ${currentState.age}, " +
                            "email = ${currentState.email}"
                )
                // ToDo: handle user insertion properly
                clearForm()
                onViewModelEffect(CreateViewModelEffect.ShowUserCreatedSnackbar)
            }
            is CreateViewEvent.OnAgeChanged -> {
                updateState(currentState.copy(age = newEvent.newValue))
            }
            is CreateViewEvent.OnEmailChanged -> {
                updateState(currentState.copy(email = newEvent.newValue))
            }
            is CreateViewEvent.OnNameChanged -> {
                updateState(currentState.copy(name = newEvent.newValue))
            }
        }
    }

    override fun onViewModelEffect(newEffect: CreateViewModelEffect) {
        when(newEffect) {
            CreateViewModelEffect.ShowDiscardSnackbar -> {
                onSnackbarEvent("Changes discarded!")
            }
            CreateViewModelEffect.ShowUserCreatedSnackbar -> {
                onSnackbarEvent("User created!")
            }
        }
    }

    private fun clearForm() {
        updateState(currentState.copy(name = "", age = "", email = ""))
    }
}