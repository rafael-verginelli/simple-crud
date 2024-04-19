package com.rafver.simplecrud.ui.navigation

sealed class Destinations(val name: String) {
    data object Create: Destinations(name = "create")
    data object Read: Destinations(name = "read")
}