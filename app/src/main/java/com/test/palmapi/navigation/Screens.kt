package com.test.palmapi.navigation

sealed class Screens(val route: String){
    object Login : Screens("login")
    object Home : Screens("home")
    object NewChat : Screens("newChat")
    object SavedChat : Screens("savedChat")
    object ModalCamera : Screens("modalCamera")

}