package com.rafver.create.ui

import com.rafver.core_ui.viewmodel.BaseViewModel
import com.rafver.create.R
import com.rafver.create.data.CreateResultType
import com.rafver.create.domain.usecases.ValidateUser
import com.rafver.create.ui.models.CreateUiState
import com.rafver.create.ui.models.CreateViewEvent
import com.rafver.create.ui.models.CreateViewModelEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val validateUser: ValidateUser,
    ) : BaseViewModel<CreateUiState, CreateViewEvent, CreateViewModelEffect>(CreateUiState())
{
    override suspend fun handleViewEvent(event: CreateViewEvent) {
        when(event) {
            CreateViewEvent.OnDiscardClicked -> {
                clearForm()
                onViewModelEffect(
                    CreateViewModelEffect.DisplaySnackbar(
                        resId = R.string.snackbar_msg_changes_discarded,
                    )
                )
                onViewModelEffect(CreateViewModelEffect.OnNameTextInputFocusRequest)
            }
            CreateViewEvent.OnCreateClicked -> {
                val validationErrors = validateUser(
                    name = currentState.name,
                    age = currentState.age,
                    email = currentState.email,
                )
                if(validationErrors.isEmpty()) {
                    clearForm()
                    onViewModelEffect(
                        CreateViewModelEffect.DisplaySnackbar(
                            resId = R.string.snackbar_msg_user_created,
                        )
                    )
                } else {
                    validationErrors.forEach { error ->
                        when(error) {
                            is CreateResultType.Error.AgeMandatory -> {
                                updateState(
                                    currentState.copy(
                                        errors = currentState.errors.copy(mandatoryAgeError = error.resId)
                                    )
                                )
                            }
                            is CreateResultType.Error.EmailMandatory -> {
                                updateState(
                                    currentState.copy(
                                        errors = currentState.errors.copy(mandatoryEmailError = error.resId)
                                    )
                                )
                            }
                            is CreateResultType.Error.InvalidAge -> {
                                updateState(
                                    currentState.copy(
                                        errors = currentState.errors.copy(invalidAgeError = error.resId)
                                    )
                                )
                            }
                            is CreateResultType.Error.NameMandatory -> {
                                updateState(
                                    currentState.copy(
                                        errors = currentState.errors.copy(mandatoryNameError = error.resId)
                                    )
                                )
                            }
                        }
                    }
                }
            }
            is CreateViewEvent.OnAgeChanged -> {
                updateState(currentState.copy(
                    age = event.newValue,
                    errors = currentState.errors.copy(mandatoryAgeError = null, invalidAgeError = null)
                ))
            }
            is CreateViewEvent.OnEmailChanged -> {
                updateState(currentState.copy(
                    email = event.newValue,
                    errors = currentState.errors.copy(mandatoryEmailError = null)
                ))
            }
            is CreateViewEvent.OnNameChanged -> {
                updateState(currentState.copy(
                    name = event.newValue,
                    errors = currentState.errors.copy(mandatoryNameError = null)
                ))
            }
        }
    }

    private fun clearForm() {
        updateState(currentState.copy(name = "", age = "", email = ""))
    }
}