@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rafver.core_ui.theme.Dimensions
import com.rafver.core_ui.theme.SimpleCRUDTheme

@Composable
fun CreateScreen() {
    Scaffold(topBar = { CreateTopBar() }) { padding ->
        Content(modifier = Modifier.padding(padding))
    }
}

@Composable
private fun CreateTopBar() {
    TopAppBar(
        title = { Text("Create New User") }
    )
}

@Composable
private fun Content(modifier: Modifier = Modifier) {
    Surface(modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimensions.NORMAL_100),
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimensions.NORMAL_100)
                .verticalScroll(state = rememberScrollState())
        ) {
            TextField(
                value = "Name",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                value = "Age",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                value = "Email",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { /*TODO*/ }
                ) {
                    Text(text = "Discard")
                }
                Spacer(modifier = Modifier.size(Dimensions.NORMAL_100))
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { /*TODO*/ }
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