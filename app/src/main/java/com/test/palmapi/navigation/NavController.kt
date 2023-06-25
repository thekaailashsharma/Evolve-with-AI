package com.test.palmapi.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.test.palmapi.MainViewModel
import com.test.palmapi.login.LoginScreen
import com.test.palmapi.newChat.NewChat

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavController() {
    val navController = rememberAnimatedNavController()
//    val viewModel: MainViewModel = hiltViewModel()
    AnimatedNavHost(navController = navController, startDestination = Screens.Login.route){
        composable(Screens.Login.route){
            LoginScreen(navHostController = navController)
        }
        composable(Screens.NewChat.route){
            NewChat(navHostController = navController)
        }

    }

}