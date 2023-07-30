package com.test.palmapi.navigation

import android.content.Intent
import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.palmapi.MainViewModel
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.home.HomeScreen
import com.test.palmapi.login.LoginScreen
import com.test.palmapi.mlkit.ModalCamera
import com.test.palmapi.mlkit.barcode.ui.BarCodeOnly
import com.test.palmapi.mlkit.textRecognition.ui.TextROnly
import com.test.palmapi.newChat.NewChat
import com.test.palmapi.prompts.PromptScreen
import com.test.palmapi.savedChat.SavedChat
import com.test.palmapi.services.OurServices
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavController(dynamicLink: String) {
    val navController = rememberAnimatedNavController()
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
                type = it.type
            }
        }
    }

    AnimatedNavHost(
        navController = navController,
//        startDestination = Screens.OurServices.route
        startDestination = if (user != null) Screens.Home.route else Screens.Login.route
    ) {
        composable(Screens.Login.route) {
            LoginScreen(navHostController = navController, viewModel = viewModel)
        }
        setComposable(Screens.NewChat.route) {
            NewChat(
                navHostController = navController,
                photoUrl = pfp.value,
                name = name.value,
                email = email.value,
                viewModel = viewModel,
                uid = uid.value,
            )
        }

        setComposable(
            Screens.SavedChat.route,

        ) {

            SavedChat(
                viewModel = viewModel,
                navController = navController,
                photoUrl = pfp.value,
                uid = uid.value,
            )
        }

        setComposable(Screens.Home.route) {
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

        setComposable(Screens.ModalCamera.route) {
            ModalCamera()
        }

        setComposable(Screens.OurServices.route) {
            OurServices(navController = navController)
        }

        setComposable(Screens.BarCodeOnly.route) {
            BarCodeOnly()
        }

        setComposable(Screens.TextROnly.route) {
            TextROnly()
        }

        setComposable(Screens.PromptChat.route, deepLinks = listOf(
            navDeepLink {
                uriPattern = "palmapi.page.link"
                action = Intent.ACTION_VIEW
            }
        )){
            PromptScreen(
                viewModel = viewModel,
                navController = navController,
                photoUrl = pfp.value,
                uid = uid.value,
                prompt = dynamicLink.substringAfter("contentt=").substringBefore("+emotion="),
                emotion = dynamicLink.substringAfter("+emotion=")
            )
        }



    }

}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.setComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    return composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Left, animationSpec = tween(300)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentScope.SlideDirection.Right, animationSpec = tween(300)
            )
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(300))
        },
        content = content
    )
}