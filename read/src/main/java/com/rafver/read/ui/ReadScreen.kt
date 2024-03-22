@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
package com.rafver.read.ui

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rafver.core_ui.theme.Dimensions
import com.rafver.core_ui.theme.SimpleCRUDTheme
import com.rafver.read.ui.models.UserUIModel
import com.rafver.read.ui.widgets.UserListItem

@Composable
fun ReadScreen(
    userList: List<UserUIModel>,
    onClickEvent: () -> Unit,
) {
    Scaffold(
        topBar = { ReadTopBar() }
    ) { padding ->
        Content(
            userList = userList,
            modifier = Modifier.padding(padding),
            onClickEvent = onClickEvent,
        )
    }
}

@Composable
private fun ReadTopBar() {
    TopAppBar(
        title = { Text("User List") }
    )
}

@Composable
private fun Content(
    userList: List<UserUIModel>,
    modifier: Modifier = Modifier,
    onClickEvent: () -> Unit,
) {
    Surface(modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Dimensions.NORMAL_100),
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.NORMAL_100)
        ) {
            items(userList) { user ->
                UserListItem(
                    user = user,
                    onClickEvent = onClickEvent,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewReadScreen() {
    SimpleCRUDTheme {
        ReadScreen(
            userList = listOf(
                UserUIModel("John", 30, "john@doe.com"),
                UserUIModel("Audrey", 40, "audrey@hepburn.com"),
                UserUIModel("Jane", 32, "jane@doe.com"),
                UserUIModel("James", 50, "james@dean.com"),
            ),
            onClickEvent = {},
        )
    }
}