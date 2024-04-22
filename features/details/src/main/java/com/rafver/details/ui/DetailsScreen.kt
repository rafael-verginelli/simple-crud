package com.rafver.details.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rafver.core_ui.extensions.collectUiState
import com.rafver.core_ui.models.UserUiModel
import com.rafver.core_ui.theme.Dimensions
import com.rafver.core_ui.theme.SimpleCRUDTheme
import com.rafver.create.ui.navigation.navigateToEdit
import com.rafver.details.R

@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: DetailsViewModel = hiltViewModel<DetailsViewModel>(),
) {
    val uiState by viewModel.collectUiState()
    val onViewEvent = viewModel::onViewEvent
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = viewModel.effects) {
        viewModel.effects.collect { effect ->
            when(effect) {
                is DetailsViewModelEffect.DisplaySnackbar -> {
                    snackbarHostState.showSnackbar(context.getString(effect.resId))
                }
                is DetailsViewModelEffect.NavigateToEdit -> {
                    navController.navigateToEdit(effect.userId)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            DetailsTopBar(
                uiState = uiState,
            )
        }
    ) { padding ->
        DetailsContent(
            modifier = Modifier.padding(padding),
            uiState = uiState,
            onViewEvent = onViewEvent,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    uiState: DetailsUiState,
) {
    TopAppBar(
        title = {
            Text(text = uiState.userModel?.name ?: stringResource(id = R.string.user_not_found))
        },
        navigationIcon = {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
        },
    )
}

@Composable
private fun DetailsContent(
    modifier: Modifier = Modifier,
    uiState: DetailsUiState,
    onViewEvent: (DetailsViewEvent) -> Unit,
) {
    if(uiState.loading) {
        //ToDo: implement loading spinner
        return
    }

    Surface(modifier = modifier) {
        if (uiState.userModel == null) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.NORMAL_100)
            ) {
                Text(stringResource(id = R.string.user_not_found))
            }
            return@Surface
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.NORMAL_100),
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.NORMAL_100)
                .verticalScroll(state = rememberScrollState())
        ) {
            Row {
                Text(
                    text = stringResource(id = R.string.lbl_name),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.size(Dimensions.SMALL_100))
                Text(text = uiState.userModel.name)
            }
            Row {
                Text(
                    text = stringResource(id = R.string.lbl_age),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.size(Dimensions.SMALL_100))
                Text(text = uiState.userModel.age.toString())
            }
            Row {
                Text(
                    text = stringResource(id = R.string.lbl_email),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.size(Dimensions.SMALL_100))
                Text(text = uiState.userModel.email)
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onViewEvent(DetailsViewEvent.OnEditClicked) }
                ) {
                    Text(text = stringResource(id = R.string.action_edit))
                }
                Spacer(modifier = Modifier.size(Dimensions.NORMAL_100))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { onViewEvent(DetailsViewEvent.OnDeleteClicked) }
                ) {
                    Text(text = stringResource(id = R.string.action_delete))
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDetailsContent() {
    SimpleCRUDTheme {
        DetailsContent(
            uiState = DetailsUiState(
                userModel = UserUiModel(
                    id = "1",
                    name = "John",
                    age = 20,
                    email = "john@doe.com"
                ),
                loading = false
            ),
            onViewEvent = {},
        )
    }
}