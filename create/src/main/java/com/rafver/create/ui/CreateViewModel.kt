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
                val result = validateUser(
                    name = currentState.name,
                    age = currentState.age,
                    email = currentState.email,
                )

                when(result) {
                    CreateResultType.Ok -> {
                        clearForm()
                        onViewModelEffect(
                            CreateViewModelEffect.DisplaySnackbar(
                                resId = R.string.snackbar_msg_user_created,
                            )
                        )
                    }
                    is CreateResultType.Error.AgeMandatory -> {
                        updateState(
                            currentState.copy(
                                errors = currentState.errors.copy(mandatoryAgeError = result)
                            )
                        )
                    }
                    is CreateResultType.Error.EmailMandatory -> {
                        updateState(
                            currentState.copy(
                                errors = currentState.errors.copy(mandatoryEmailError = result)
                            )
                        )
                    }
                    is CreateResultType.Error.InvalidAge -> {
                        updateState(
                            currentState.copy(
                                errors = currentState.errors.copy(invalidAgeError = result)
                            )
                        )
                    }
                    is CreateResultType.Error.NameMandatory -> {
                        updateState(
                            currentState.copy(
                                errors = currentState.errors.copy(mandatoryNameError = result)
                            )
                        )
                    }
                }
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