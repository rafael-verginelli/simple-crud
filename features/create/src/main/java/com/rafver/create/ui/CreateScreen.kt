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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafver.core_ui.extensions.collectUiState
import com.rafver.core_ui.theme.Dimensions
import com.rafver.core_ui.theme.SimpleCRUDTheme
import com.rafver.create.R
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

    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(key1 = viewModel.effects) {
        viewModel.effects.collect { effect ->
            when(effect) {
                is CreateViewModelEffect.DisplaySnackbar -> {
                    snackbarHostState.showSnackbar(context.getString(effect.resId))
                }

                CreateViewModelEffect.OnNameTextInputFocusRequest -> {
                    focusRequester.freeFocus()
                }
            }
        }
    }

    Scaffold(
        topBar = { CreateTopBar() },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        CreateContent(
            uiState = uiState,
            onViewEvent = onViewEvent,
            focusRequester = focusRequester,
            modifier = Modifier.padding(padding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateTopBar() {
    TopAppBar(
        title = { Text(stringResource(id = R.string.title_create_user)) }
    )
}

@Composable
private fun CreateContent(
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
                label = { Text(stringResource(id = R.string.lbl_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                isError = uiState.errors.mandatoryNameError != null,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
                supportingText = {
                    uiState.errors.mandatoryNameError?.let {
                        Text(text = stringResource(id = it))
                    }
                }
            )
            OutlinedTextField(
                value = uiState.age,
                onValueChange = { newValue -> onViewEvent(CreateViewEvent.OnAgeChanged(newValue)) },
                label = { Text(stringResource(id = R.string.lbl_age)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                isError = uiState.errors.mandatoryAgeError != null || uiState.errors.invalidAgeError != null,
                supportingText = {
                    uiState.errors.mandatoryAgeError?.let {
                        Text(text = stringResource(id = it))
                    }
                    uiState.errors.invalidAgeError?.let {
                        Text(text = stringResource(id = it))
                    }
                }
            )
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { newValue -> onViewEvent(CreateViewEvent.OnEmailChanged(newValue)) },
                label = { Text(stringResource(id = R.string.lbl_email)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    capitalization = KeyboardCapitalization.None,
                    imeAction = ImeAction.Done,
                ),
                isError = uiState.errors.mandatoryEmailError != null,
                supportingText = {
                    uiState.errors.mandatoryEmailError?.let {
                        Text(text = stringResource(id = it))
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    enabled = uiState.name.isNotEmpty() || uiState.age.isNotEmpty() || uiState.email.isNotEmpty(),
                    modifier = Modifier.weight(1f),
                    onClick = { onViewEvent(CreateViewEvent.OnDiscardClicked) }
                ) {
                    Text(text = stringResource(id = R.string.action_discard))
                }
                Spacer(modifier = Modifier.size(Dimensions.NORMAL_100))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onViewEvent(CreateViewEvent.OnCreateClicked) }
                ) {
                    Text(text = stringResource(id = R.string.action_create))
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