@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.rafver.create.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafver.core_ui.extensions.collectUiState
import com.rafver.core_ui.theme.Dimensions
import com.rafver.core_ui.theme.SimpleCRUDTheme
import com.rafver.create.ui.models.CreateUiState
import com.rafver.create.ui.models.CreateViewEvent
import com.rafver.create.ui.models.CreateViewModelEffect

@Composable
fun CreateScreen(
    viewModel: CreateViewModel = viewModel()
) {
    val uiState by viewModel.collectUiState()
    val onViewEvent = viewModel::onViewEvent

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = viewModel.effects) {
        viewModel.effects.collect { effect ->
            when(effect) {
                CreateViewModelEffect.OnNameTextInputFocusRequest -> {
                    focusRequester.freeFocus()
                }
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarEvent by viewModel.snackbarEvent.collectAsState()
//    snackbarEvent?.handleSingleEvent()?.let { message ->
//        LaunchedEffect(message) {
//            snackbarHostState.showSnackbar(message)
//        }
//    }
    LaunchedEffect(snackbarEvent) {
        snackbarEvent?.handleSingleEvent()?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        topBar = { CreateTopBar() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Content(
            uiState = uiState,
            onViewEvent = onViewEvent,
            focusRequester = focusRequester,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun CreateTopBar() {
    TopAppBar(
        title = { Text("Create New User") }
    )
}

@Composable
private fun Content(
    uiState: CreateUiState,
    onViewEvent: (CreateViewEvent) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.NORMAL_100),
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.NORMAL_100)
                .verticalScroll(state = rememberScrollState())
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { newValue -> onViewEvent(CreateViewEvent.OnNameChanged(newValue)) },
                label = { Text("Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )
            OutlinedTextField(
                value = uiState.age,
                onValueChange = { newValue -> onViewEvent(CreateViewEvent.OnAgeChanged(newValue)) },
                label = { Text("Age") },
                modifier = Modifier
                    .fillMaxWidth()
                .focusRequester(focusRequester),
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { newValue -> onViewEvent(CreateViewEvent.OnEmailChanged(newValue)) },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onViewEvent(CreateViewEvent.OnDiscardClicked) }
                ) {
                    Text(text = "Discard")
                }
                Spacer(modifier = Modifier.size(Dimensions.NORMAL_100))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onViewEvent(CreateViewEvent.OnCreateClicked) }
                ) {
                    Text(text = "Create")
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCreateScreen() {
    SimpleCRUDTheme {
        CreateScreen()
    }
}