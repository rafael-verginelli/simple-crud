package com.rafver.create.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(): ViewModel() {

    fun onSaveClicked() {
        println("OnSaveClicked")
    }

    fun onDiscardClicked() {
        println("OnDiscardClicked")
    }
}