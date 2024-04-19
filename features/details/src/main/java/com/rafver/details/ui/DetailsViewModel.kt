package com.rafver.details.ui

import androidx.lifecycle.SavedStateHandle
import com.rafver.core_ui.viewmodel.BaseViewModel
import com.rafver.details.ui.navigation.DetailsArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<DetailsUiState, DetailsViewEvent, DetailsViewModelEffect>(
        DetailsUiState()
    ) {

    private val detailArgs = DetailsArgs(savedStateHandle)

    init {
        println("details args: ${detailArgs.userId}")
    }

    override suspend fun handleViewEvent(event: DetailsViewEvent) {
        when(event) {
            DetailsViewEvent.OnDeleteClicked -> {
                TODO()
            }
            DetailsViewEvent.OnEditClicked -> {
                TODO()
            }
        }
    }

}