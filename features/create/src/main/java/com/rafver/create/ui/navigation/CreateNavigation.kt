package com.rafver.create.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rafver.create.ui.CreateScreen
import com.rafver.create.ui.CreateViewModel

private const val navigationRoute = "create"
private const val userIdArg = "userId"

fun NavGraphBuilder.editScreen() {
    composable("${navigationRoute}/{$userIdArg}") {
        val viewModel: CreateViewModel = hiltViewModel<CreateViewModel>()
        CreateScreen(viewModel = viewModel)
    }
}

fun NavController.navigateToEdit(userId: String) {
    this.navigate("${navigationRoute}/${userId}")
}

internal class EditArgs(val userId: String?) {
    constructor(savedStateHandle: SavedStateHandle): this(savedStateHandle.get<String>(userIdArg))
}
