package com.rafver.simplecrud.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home

data object MainBottomNavigation {
    fun getNavigationItems(): List<MainBottomNavigationItem> {
        return listOf(
            MainBottomNavigationItem(
                label = "Home", // ToDo use extracted string
                icon = Icons.Filled.Home,
                route = Destinations.Read.name,
            ),
            MainBottomNavigationItem(
                label = "Create", // ToDo use extracted string
                icon = Icons.Filled.Add,
                route = Destinations.Create.name,
            ),
        )
    }
}