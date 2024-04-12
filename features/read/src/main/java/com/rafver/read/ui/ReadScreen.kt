@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
package com.rafver.read.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rafver.core_ui.extensions.collectUiState
import com.rafver.core_ui.theme.Dimensions
import com.rafver.core_ui.theme.SimpleCRUDTheme
import com.rafver.read.R
import com.rafver.read.ui.models.ReadUiState
import com.rafver.read.ui.models.ReadViewEvent
import com.rafver.core_ui.models.UserUiModel
import com.rafver.read.ui.widgets.UserListItem

@Composable
fun ReadScreen(
    viewModel: ReadViewModel = viewModel()
) {
    val uiState by viewModel.collectUiState()
    val onViewEvent = viewModel::onViewEvent

    Scaffold(
        topBar = { ReadTopBar() }
    ) { padding ->
        ReadContent(
            uiState = uiState,
            onViewEvent = onViewEvent,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
private fun ReadTopBar() {
    TopAppBar(
        title = { Text(stringResource(id = R.string.title_read_user)) }
    )
}

@Composable
private fun ReadContent(
    uiState: ReadUiState,
    onViewEvent: (ReadViewEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        if(uiState.loading) {
            //ToDo: loading widget here
        } else {
            with(uiState.userList) {
                if (isNullOrEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(stringResource(id = R.string.empty_list))
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Dimensions.NORMAL_100),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(Dimensions.NORMAL_100)
                    ) {
                        items(this@with) { user ->
                            UserListItem(
                                user = user,
                                onViewEvent = onViewEvent,
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun PreviewReadScreenContent() {
    SimpleCRUDTheme {
        ReadContent(
            uiState = ReadUiState(
                userList = listOf(
                    UserUiModel("1", "John", 30, "john@doe.com"),
                    UserUiModel("2", "Audrey", 40, "audrey@hepburn.com"),
                    UserUiModel("3", "Jane", 32, "jane@doe.com"),
                    UserUiModel("4", "James", 50, "james@dean.com"),
                ),
            ),
            onViewEvent = {},
        )
    }
}