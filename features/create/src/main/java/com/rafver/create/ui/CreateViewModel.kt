package com.rafver.create.ui

import androidx.lifecycle.SavedStateHandle
import com.rafver.core_data.utils.createLogger
import com.rafver.core_domain.usecases.GetUser
import com.rafver.core_ui.viewmodel.BaseViewModel
import com.rafver.create.R
import com.rafver.create.data.CreateResultType
import com.rafver.create.domain.usecases.CreateUser
import com.rafver.create.domain.usecases.UpdateUser
import com.rafver.create.domain.usecases.ValidateUser
import com.rafver.create.ui.models.CreateUiState
import com.rafver.create.ui.models.CreateViewEvent
import com.rafver.create.ui.models.CreateViewModelEffect
import com.rafver.create.ui.navigation.EditArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val validateUser: ValidateUser,
    private val createUser: CreateUser,
    private val updateUser: UpdateUser,
    private val getUser: GetUser,
) : BaseViewModel<CreateUiState, CreateViewEvent, CreateViewModelEffect>(CreateUiState())
{
    private val logger by lazy { createLogger() }
    private val editArgs = EditArgs(savedStateHandle)

    init {
        onViewEvent(CreateViewEvent.OnInitialize)
    }

    override suspend fun handleViewEvent(event: CreateViewEvent) {
        when(event) {
            CreateViewEvent.OnInitialize -> {
                if(editArgs.userId != null) {
                    updateState(currentState.copy(loading = true))
                    val result = getUser(editArgs.userId)
                    val user = result.getOrNull()
                    if(user != null) {
                        updateState(currentState.copy(
                            isEditMode = true,
                            name = user.name,
                            age = user.age.toString(),
                            email = user.email,
                            loading = false,
                        ))
                    } else {
                        updateState(currentState.copy(loading = false))
                        handleException(Exception("User not found"))
                    }
                }
            }
            CreateViewEvent.OnDiscardClicked -> {
                clearForm()
                onViewModelEffect(
                    CreateViewModelEffect.DisplaySnackbar(
                        resId = R.string.snackbar_msg_changes_discarded,
                    )
                )
                onViewModelEffect(CreateViewModelEffect.OnNameTextInputFocusRequest)
            }
            CreateViewEvent.OnCreateClicked,
            CreateViewEvent.OnUpdateClicked -> {
                updateState(currentState.copy(loading = true))
                val validationErrors = validateUser(
                    name = currentState.name,
                    age = currentState.age,
                    email = currentState.email,
                )
                if(validationErrors.isEmpty()) {
                    val messageResId: Int
                    val result =
                        when(event) {
                            CreateViewEvent.OnCreateClicked -> {
                                messageResId = R.string.snackbar_msg_user_created
                                createUser(currentState.name, currentState.age, currentState.email)
                            }
                            CreateViewEvent.OnUpdateClicked -> {
                                val userId = editArgs.userId ?: throw IllegalStateException("Missing userId")
                                messageResId = R.string.snackbar_msg_user_updated
                                updateUser(userId, currentState.name, currentState.age, currentState.email)
                            }
                            else -> {
                                throw IllegalStateException("Operation can only be either CreateUser or UpdateUser here.")
                            }
                        }
                    if(result.isSuccess) {
                        updateState(currentState.copy(loading = false))
                        clearForm()
                        onViewModelEffect(
                            CreateViewModelEffect.DisplaySnackbar(resId = messageResId)
                        )
                    } else {
                        updateState(currentState.copy(loading = false))
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
                    updateState(currentState.copy(loading = false))
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

    override fun handleException(error: Throwable?) {
        logger.d("An error has occurred.", error)
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