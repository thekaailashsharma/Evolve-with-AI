package com.test.palmapi.navigation

import android.content.Intent
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.palmapi.MainViewModel
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.devices.DevicesScreen
import com.test.palmapi.home.HomeScreen
import com.test.palmapi.login.LoginScreen
import com.test.palmapi.mlkit.ModalCamera
import com.test.palmapi.mlkit.barcode.ui.BarCodeOnly
import com.test.palmapi.mlkit.textRecognition.ui.TextROnly
import com.test.palmapi.newChat.NewChat
import com.test.palmapi.prompts.PromptScreen
import com.test.palmapi.savedChat.SavedChat
import com.test.palmapi.services.OurServices
import com.test.palmapi.textToImage.TextToImageScreen
import com.test.palmapi.theming.MultipleThemes
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavController(dynamicLink: String) {
    val navController = rememberNavController()
    val user by remember { mutableStateOf(Firebase.auth.currentUser) }
    user?.let {
        for (profile in it.providerData) {
            Log.i("Email-Profile2", profile.email.toString())

        }
    }
    val viewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
    val dataStore = UserDatastore(context)
    val name = dataStore.getName.collectAsState(initial = "")
    val email = dataStore.getEmail.collectAsState(initial = "")
    Log.i("Name", name.value)
    val pfp = dataStore.getPfp.collectAsState(initial = "")
    val uid = dataStore.getUID.collectAsState(initial = "")
    var type by remember { mutableStateOf("") }
    LaunchedEffect(key1 = viewModel.getType(uid.value)) {
        if (uid.value != "") {
            viewModel.getType(uid.value).collectLatest {
                Log.i("Typessssssssss", it.type)
                type = it.type
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (user != null) Screens.Home.route else Screens.Login.route,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300),
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(300),
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(300),
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300),
            )
        },

        ) {
        composable(Screens.Login.route) {
            LoginScreen(navHostController = navController, viewModel = viewModel)
        }
        composable(Screens.NewChat.route) {
            NewChat(
                navHostController = navController,
                photoUrl = pfp.value,
                name = name.value,
                email = email.value,
                viewModel = viewModel,
                uid = uid.value,
            )
        }

        composable(
            Screens.SavedChat.route,

            ) {

            SavedChat(
                viewModel = viewModel,
                navController = navController,
                photoUrl = pfp.value,
                uid = uid.value,
            )
        }

        composable(Screens.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                photoUrl = pfp.value,
                name = name.value,
                email = email.value,
                navHostController = navController,
                uid = uid.value,
                type = type
            )
        }

        composable(Screens.ModalCamera.route) {
            ModalCamera()
        }

        composable(Screens.OurServices.route) {
            OurServices(navController = navController)
        }

        composable(Screens.BarCodeOnly.route) {
            BarCodeOnly()
        }

        composable(Screens.TextROnly.route) {
            TextROnly()
        }

        composable(Screens.PromptChat.route, deepLinks = listOf(
            navDeepLink {
                uriPattern = "palmapi.page.link"
                action = Intent.ACTION_VIEW
            }
        )) {
            PromptScreen(
                viewModel = viewModel,
                navController = navController,
                photoUrl = pfp.value,
                uid = uid.value,
                prompt = dynamicLink.substringAfter("contentt=").substringBefore("+emotion="),
                emotion = dynamicLink.substringAfter("+emotion=")
            )
        }
        composable(Screens.Themes.route) {
            MultipleThemes(navController = navController)
        }

        composable(Screens.TextToImage.route) {
            TextToImageScreen(
                viewModel = viewModel,
                navHostController = navController,
                userPfp = pfp.value
            )
        }

        composable(Screens.Devices.route){
            DevicesScreen(
                email = email.value,
                viewModel = viewModel,
                pfp = pfp.value,
                name = name.value,
                navHostController = navController
            )
        }
    }


}

