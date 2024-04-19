package com.rafver.details.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rafver.details.ui.DetailsScreen
import com.rafver.details.ui.DetailsViewModel

private const val navigationRoute = "details"
private const val userIdArg = "userId"

fun NavGraphBuilder.detailsScreen() {
    composable("${navigationRoute}/{$userIdArg}") {
        val viewModel: DetailsViewModel = hiltViewModel<DetailsViewModel>()
        DetailsScreen(viewModel = viewModel)
    }
}

fun NavController.navigateToDetails(userId: String) {
    this.navigate("${navigationRoute}/{$userId}")
}

internal class DetailsArgs(val userId: String) {
    constructor(savedStateHandle: SavedStateHandle)
            : this(checkNotNull(savedStateHandle[userIdArg]) as String)
}

