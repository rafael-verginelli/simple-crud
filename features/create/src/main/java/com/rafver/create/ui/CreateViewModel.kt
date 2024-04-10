package com.rafver.create.ui

import com.rafver.core_ui.viewmodel.BaseViewModel
import com.rafver.create.R
import com.rafver.create.data.CreateResultType
import com.rafver.create.domain.usecases.CreateUser
import com.rafver.create.domain.usecases.ValidateUser
import com.rafver.create.ui.models.CreateUiState
import com.rafver.create.ui.models.CreateViewEvent
import com.rafver.create.ui.models.CreateViewModelEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val validateUser: ValidateUser,
    private val createUser: CreateUser,
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
                    val result = createUser(currentState.name, currentState.age, currentState.email)
                    if(result.isSuccess) {
                        clearForm()
                        onViewModelEffect(
                            CreateViewModelEffect.DisplaySnackbar(
                                resId = R.string.snackbar_msg_user_created,
                            )
                        )
                    } else {
                        val error = result.exceptionOrNull()
                        handleException(error)
                    }

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

    private fun handleException(error: Throwable?) {
        println("An error has occurred: ${error?.message}")
        when(error) {
            else -> {
                onViewModelEffect(
                    CreateViewModelEffect.DisplaySnackbar(
                        resId = R.string.error_create_generic,
                    )
                )
            }
        }
    }

    private fun clearForm() {
        updateState(currentState.copy(name = "", age = "", email = ""))
    }
}