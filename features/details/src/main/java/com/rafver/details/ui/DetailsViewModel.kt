package com.rafver.details.ui

import androidx.lifecycle.SavedStateHandle
import com.rafver.core_domain.usecases.GetUser
import com.rafver.core_ui.models.toUiModel
import com.rafver.core_ui.viewmodel.BaseViewModel
import com.rafver.details.R
import com.rafver.details.domain.usecases.DeleteUser
import com.rafver.details.ui.navigation.DetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUser: GetUser,
    private val deleteUser: DeleteUser,
) : BaseViewModel<DetailsUiState, DetailsViewEvent, DetailsViewModelEffect>(
    DetailsUiState()
) {

    private val detailArgs = DetailsArgs(savedStateHandle)

    init {
        onViewEvent(DetailsViewEvent.OnInitialize)
    }

    override suspend fun handleViewEvent(event: DetailsViewEvent) {
        when(event) {
            DetailsViewEvent.OnInitialize -> {
                val result = getUser(detailArgs.userId)
                val user = result.getOrNull()
                if(user != null) {
                    updateState(currentState.copy(userModel = user.toUiModel()))
                } else {
                    handleException(result.exceptionOrNull())
                }
            }
            DetailsViewEvent.OnDeleteClicked -> {
                updateState(currentState.copy(showDeleteDialog = true))
            }
            DetailsViewEvent.OnDeleteCancelClicked -> {
                updateState(currentState.copy(showDeleteDialog = false))
            }
            DetailsViewEvent.OnDeleteConfirmationClicked -> {
                updateState(currentState.copy(showDeleteDialog = false))
                val result = deleteUser(detailArgs.userId)
                if(result.isSuccess) {
                    // ToDo: implement "user deleted" message
                    onViewModelEffect(DetailsViewModelEffect.NavigateUp)
                } else {
                    val error = result.exceptionOrNull()
                    handleException(error)
                }
            }
            DetailsViewEvent.OnEditClicked -> {
                onViewModelEffect(DetailsViewModelEffect.NavigateToEdit(detailArgs.userId))
            }
        }
    }

    override fun handleException(error: Throwable?) {
        println("An error has occurred: ${error?.message}")
        when(error) {
            else -> {
                onViewModelEffect(
                    DetailsViewModelEffect.DisplaySnackbar(
                        resId = R.string.error_details_generic,
                    )
                )
            }
        }
    }

}