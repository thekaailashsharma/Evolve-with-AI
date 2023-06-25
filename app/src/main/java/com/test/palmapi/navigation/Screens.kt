package com.test.palmapi.navigation

sealed class Screens(val route: String){
    object Login: Screens("login")
    object Home: Screens("home")
    object ApiDetail: Screens("apiDetail")
    object NewChat : Screens("newChat")
}