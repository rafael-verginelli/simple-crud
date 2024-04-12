package com.rafver.details.ui

import com.rafver.core_ui.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor()
    : BaseViewModel<DetailsUiState, DetailsViewEvent, DetailsViewModelEffect>(
        DetailsUiState()
    ) {

    override suspend fun handleViewEvent(event: DetailsViewEvent) {
        when(event) {
            DetailsViewEvent.OnDeleteClicked -> TODO()
            DetailsViewEvent.OnEditClicked -> TODO()
        }
    }

}