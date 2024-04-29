package com.rafver.read.ui

import com.rafver.core_data.utils.createLogger
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
) : BaseViewModel<ReadUiState, ReadViewEvent, ReadViewModelEffect>(ReadUiState()) {

    private val logger by lazy { createLogger() }

    init {
        onViewEvent(ReadViewEvent.OnInitialize)
    }

    override suspend fun handleViewEvent(event: ReadViewEvent) {
        when(event) {
            is ReadViewEvent.OnListItemClicked -> {
                onViewModelEffect(ReadViewModelEffect.NavigateToDetails(event.userId))
            }
            ReadViewEvent.OnInitialize -> {
                updateState(ReadUiState(loading = true))
                val result = getUserList()
                if(result.isFailure) {
                    updateState(currentState.copy(loading = false))
                    handleException(result.exceptionOrNull())
                } else {
                    updateState(currentState.copy(
                        loading = false,
                        userList = result.getOrNull()?.toUiModel()
                    ))
                }
            }
        }
    }

    override fun handleException(error: Throwable?) {
        logger.d("An error has occurred.", error)

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