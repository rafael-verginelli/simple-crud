package com.rafver.simplecrud.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rafver.core_ui.extensions.collectUiState
import com.rafver.core_ui.theme.SimpleCRUDTheme
import com.rafver.create.ui.CreateScreen
import com.rafver.create.ui.CreateViewModel
import com.rafver.create.ui.navigation.editScreen
import com.rafver.details.ui.navigation.detailsScreen
import com.rafver.read.ui.ReadScreen
import com.rafver.read.ui.ReadViewModel
import com.rafver.simplecrud.ui.navigation.Destinations
import com.rafver.simplecrud.ui.navigation.MainBottomNavigation

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel<MainViewModel>(),
) {
    val uiState by viewModel.collectUiState()
    val onViewEvent = viewModel::onViewEvent

    val navController = rememberNavController()

    LaunchedEffect(key1 = viewModel.effects) {
        viewModel.effects.collect { effect ->
            when(effect) {
                is MainViewModelEffect.NavigateTo -> navController.navigate(effect.route)
            }
        }
    }

    Scaffold(
        bottomBar = {
            MainBottomBar(
                uiState = uiState,
                onViewEvent = onViewEvent,
                navController = navController,
            )
        },
    ) { paddingValues ->
        MainContent(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun MainContent(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        NavHost(navController = navController, startDestination = Destinations.Read.name) {
            composable(Destinations.Create.name) { CreateScreen(viewModel = hiltViewModel<CreateViewModel>()) }
            composable(Destinations.Read.name) {
                ReadScreen(
                    navController = navController,
                    viewModel = hiltViewModel<ReadViewModel>()
                )
            }
            detailsScreen(navController)
            editScreen()
        }
    }
}

@Composable
private fun MainBottomBar(
    uiState: MainUiState,
    onViewEvent: (MainViewEvent) -> Unit,
    navController: NavController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if(currentRoute !in MainBottomNavigation.getNavigationItems().map { it.route }) {
        return
    }

    NavigationBar {
        MainBottomNavigation.getNavigationItems().forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == uiState.currentSelectedNavigationItemIndex,
                onClick = {
                    onViewEvent(MainViewEvent.OnBottomNavigationItemClicked(
                        route = item.route,
                        itemIndex = index,
                    ))
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.label)
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewMainScreen() {
    SimpleCRUDTheme {
        MainScreen()
    }
}