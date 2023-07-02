package com.test.palmapi.bottombar

import com.test.palmapi.navigation.Screens

sealed class BottomBarScreens(val route: String?, val title: String?, val icon: Int) {
    object HistoryScreen : BottomBarScreens(
        Screens.Home.route,
        "Home",
        com.test.palmapi.R.drawable.home
    )

    object GenerateScreen : BottomBarScreens(
        Screens.OurServices.route,
        "Services",
        com.test.palmapi.R.drawable.services
    )
}

val items = listOf(
    BottomBarScreens.HistoryScreen,
    BottomBarScreens.GenerateScreen
)