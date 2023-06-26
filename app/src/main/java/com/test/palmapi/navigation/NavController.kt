package com.test.palmapi.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.test.palmapi.MainViewModel
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.home.HomeScreen
import com.test.palmapi.login.LoginScreen
import com.test.palmapi.newChat.NewChat
import com.test.palmapi.savedChat.SavedChat

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavController() {
    val navController = rememberAnimatedNavController()
    val viewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
    val dataStore = UserDatastore(context)
    val name = dataStore.getName.collectAsState(initial = "")
    val email = dataStore.getEmail.collectAsState(initial = "")
    val pfp = dataStore.getPfp.collectAsState(initial = "")
    AnimatedNavHost(navController = navController, startDestination = Screens.Login.route) {
        composable(Screens.Login.route) {
            LoginScreen(navHostController = navController)
        }
        composable(Screens.NewChat.route) {
            NewChat(navHostController = navController, photoUrl = pfp.value, viewModel = viewModel)
        }

        composable(Screens.SavedChat.route) {
            SavedChat(viewModel = viewModel, navController = navController, photoUrl = pfp.value)
        }

        composable(Screens.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                photoUrl = pfp.value,
                name = name.value,
                email = email.value
            )
        }

    }

}