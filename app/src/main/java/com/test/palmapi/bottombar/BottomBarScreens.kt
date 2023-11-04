package com.test.palmapi.bottombar

import com.test.palmapi.navigation.Screens

sealed class BottomBarScreens(val route: String?, val title: String?, val icon: Int) {
    object DashboardScreen : BottomBarScreens(
        Screens.Home.route,
        "Home",
        com.test.palmapi.R.drawable.appicon
    )

    object GenerateScreen : BottomBarScreens(
        Screens.OurServices.route,
        "Services",
        com.test.palmapi.R.drawable.services
    )

    object ThemesScreen : BottomBarScreens(
        Screens.Themes.route,
        "Themes",
        com.test.palmapi.R.drawable.themes
    )
}

val items = listOf(
    BottomBarScreens.DashboardScreen,
    BottomBarScreens.GenerateScreen,
    BottomBarScreens.ThemesScreen
)