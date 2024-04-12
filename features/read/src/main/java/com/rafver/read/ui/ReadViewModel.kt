package com.rafver.read.ui

import com.rafver.core_ui.models.toUiModel
import com.rafver.core_ui.viewmodel.BaseViewModel
import com.rafver.read.R
import com.rafver.read.domain.usecases.GetUserList
import com.rafver.read.ui.models.ReadUiState
import com.rafver.read.ui.models.ReadViewEvent
import com.rafver.read.ui.models.ReadViewModelEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReadViewModel @Inject constructor(
    private val getUserList: GetUserList,
)
    : BaseViewModel<ReadUiState, ReadViewEvent, ReadViewModelEffect>(ReadUiState()) {

    init {
        onViewEvent(ReadViewEvent.OnInitialize)
    }

    override suspend fun handleViewEvent(event: ReadViewEvent) {
        when(event) {
            is ReadViewEvent.OnListItemClicked -> {
                println("Item ${event.id} clicked")
                // ToDo: Implement this
            }
            ReadViewEvent.OnInitialize -> {
                val result = getUserList()
                if(result.isFailure) {
                    handleException(result.exceptionOrNull())
                } else {
                    updateState(currentState.copy(userList = result.getOrNull()?.toUiModel()))
                }
            }
        }
    }

    private fun handleException(error: Throwable?) {
        println("An error has occurred: ${error?.message}")

        when(error) {
            else -> {
                onViewModelEffect(
                    ReadViewModelEffect.DisplaySnackbar(
                        resId = R.string.error_read_generic
                    )
                )
            }
        }
    }

}