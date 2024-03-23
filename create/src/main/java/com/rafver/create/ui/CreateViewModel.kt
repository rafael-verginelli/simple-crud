package com.rafver.create.ui

import com.rafver.core_ui.viewmodel.BaseViewModel
import com.rafver.create.ui.models.CreateUiState
import com.rafver.create.ui.models.CreateViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor()
    : BaseViewModel<CreateUiState, CreateViewEvent>(CreateUiState())
{
    public override fun onViewEvent(newEvent: CreateViewEvent) {
        when(newEvent) {
            CreateViewEvent.OnDiscardClicked -> {
                updateState(currentState.copy(name = "", age = "", email = ""))
                // ToDo Display toast
            }
            CreateViewEvent.OnUpdateClicked -> {
                println("Creating User with name = ${currentState.name}, age = ${currentState.age}, email = ${currentState.email}")
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
}