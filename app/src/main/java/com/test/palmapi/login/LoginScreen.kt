package com.test.palmapi.login

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.test.palmapi.MainActivity
import com.test.palmapi.MainViewModel
import com.test.palmapi.R
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.navigation.Screens
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.monteBold
import com.test.palmapi.ui.theme.monteNormal
import com.test.palmapi.ui.theme.textColor
import com.test.palmapi.ui.theme.ybc
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navHostController: NavHostController, viewModel: MainViewModel) {

    val firebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val dataStore = UserDatastore(context)
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    val githubLogin = {
        val provider = OAuthProvider.newBuilder("github.com")
        commonLoginCode(provider, firebaseAuth, context, onLogin = {
            coroutineScope.launch {
                dataStore.saveEmail(googleAuthUiClient.getSignedInUser()?.email ?: "")
                dataStore.saveName(googleAuthUiClient.getSignedInUser()?.username ?: "")
                dataStore.savePfp(googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "")
                dataStore.saveUID(googleAuthUiClient.getSignedInUser()?.uniqueId ?: "")
            }
            addAccountToManager(
                context,
                googleAuthUiClient.getSignedInUser()?.email ?: "",
                googleAuthUiClient.getSignedInUser()?.username ?: "",
                googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "",
                googleAuthUiClient.getSignedInUser()?.uniqueId ?: "",
                "github"
            )
            viewModel.addAccount(
                email = googleAuthUiClient.getSignedInUser()?.email ?: "",
                firstName = googleAuthUiClient.getSignedInUser()?.username?.substringBefore(" ")
                    ?: "",
                lastName = googleAuthUiClient.getSignedInUser()?.username?.substringAfter(" ")
                    ?: "",
                photoUrl = googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "",
                uniqueId = googleAuthUiClient.getSignedInUser()?.uniqueId ?: "",
                type = "github",
                isCurrent = true
            )
            navHostController.popBackStack()
            navHostController.navigate(Screens.NewChat.route)
        })
    }
    val twitterLogin = {
        val provider = OAuthProvider.newBuilder("twitter.com")
        commonLoginCode(provider, firebaseAuth, context, onLogin = {
            coroutineScope.launch {
                dataStore.saveEmail(googleAuthUiClient.getSignedInUser()?.email ?: "")
                dataStore.saveName(googleAuthUiClient.getSignedInUser()?.username ?: "")
                dataStore.savePfp(googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "")
                dataStore.saveUID(googleAuthUiClient.getSignedInUser()?.uniqueId ?: "")
            }
            addAccountToManager(
                context,
                googleAuthUiClient.getSignedInUser()?.email ?: "",
                googleAuthUiClient.getSignedInUser()?.username ?: "",
                googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "",
                googleAuthUiClient.getSignedInUser()?.uniqueId ?: "",
                "twitter"
            )
            viewModel.addAccount(
                email = googleAuthUiClient.getSignedInUser()?.email ?: "",
                firstName = googleAuthUiClient.getSignedInUser()?.username?.substringBefore(" ")
                    ?: "",
                lastName = googleAuthUiClient.getSignedInUser()?.username?.substringAfter(" ")
                    ?: "",
                photoUrl = googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "",
                uniqueId = googleAuthUiClient.getSignedInUser()?.uniqueId ?: "",
                type = "twitter",
                isCurrent = true
            )
            navHostController.popBackStack()
            navHostController.navigate(Screens.NewChat.route)
        })
    }

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        Log.i("TypeChanged", "isSignInSuccessful")
        if (state.isSignInSuccessful) {
            Log.i("TypeChangedCalled", "isSignInSuccessful")
            Toast.makeText(
                context,
                "Sign in successful as ${googleAuthUiClient.getSignedInUser()?.email}",
                Toast.LENGTH_LONG
            ).show()
            coroutineScope.launch {
                dataStore.saveEmail(googleAuthUiClient.getSignedInUser()?.email ?: "")
                dataStore.saveName(googleAuthUiClient.getSignedInUser()?.username ?: "")
                dataStore.savePfp(googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "")
                dataStore.saveUID(googleAuthUiClient.getSignedInUser()?.uniqueId ?: "")
                addAccountToManager(
                    context,
                    googleAuthUiClient.getSignedInUser()?.email ?: "",
                    googleAuthUiClient.getSignedInUser()?.username ?: "",
                    googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "",
                    googleAuthUiClient.getSignedInUser()?.uniqueId ?: "",
                    "google"
                )
                viewModel.addAccount(
                    email = googleAuthUiClient.getSignedInUser()?.email ?: "",
                    firstName = googleAuthUiClient.getSignedInUser()?.username?.substringBefore(" ")
                        ?: "",
                    lastName = googleAuthUiClient.getSignedInUser()?.username?.substringAfter(" ")
                        ?: "",
                    photoUrl = googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "",
                    uniqueId = googleAuthUiClient.getSignedInUser()?.uniqueId ?: "",
                    type = "google",
                    isCurrent = true
                )
            }

            navHostController.popBackStack()
            navHostController.navigate(Screens.NewChat.route)
            viewModel.resetState()
        }
    }
    LaunchedEffect(key1 = Unit) {
        Log.i("Auth-Client", googleAuthUiClient.getSignedInUser().toString())
        if (googleAuthUiClient.getSignedInUser()?.username != null) {
            Log.i("Auth-Client2.0", googleAuthUiClient.getSignedInUser()?.email ?: "")
            Log.i("Auth-Client2.0", googleAuthUiClient.getSignedInUser()?.username ?: "")
            Log.i("Auth-Client2.0", googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "")
            coroutineScope.launch {
                dataStore.saveEmail(googleAuthUiClient.getSignedInUser()?.email ?: "")
                dataStore.saveName(googleAuthUiClient.getSignedInUser()?.username ?: "")
                dataStore.savePfp(googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "")
                dataStore.saveUID(googleAuthUiClient.getSignedInUser()?.uniqueId ?: "")
                addAccountToManager(
                    context,
                    googleAuthUiClient.getSignedInUser()?.email ?: "",
                    googleAuthUiClient.getSignedInUser()?.username ?: "",
                    googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "",
                    googleAuthUiClient.getSignedInUser()?.uniqueId ?: "",
                    "google"
                )
                viewModel.addAccount(
                    email = googleAuthUiClient.getSignedInUser()?.email ?: "",
                    firstName = googleAuthUiClient.getSignedInUser()?.username?.substringBefore(" ")
                        ?: "",
                    lastName = googleAuthUiClient.getSignedInUser()?.username?.substringAfter(" ")
                        ?: "",
                    photoUrl = googleAuthUiClient.getSignedInUser()?.profilePictureUrl ?: "",
                    uniqueId = googleAuthUiClient.getSignedInUser()?.uniqueId ?: "",
                    type = "google",
                    isCurrent = true
                )
            }

            navHostController.popBackStack()
            navHostController.navigate(Screens.NewChat.route)
        }
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = appGradient, shape = RoundedCornerShape(7.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 150.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = ybc,
                        color = textColor,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black
                    )
                ) {
                    append("Sign In")
                }
                append("\n\n")
                withStyle(
                    SpanStyle(
                        fontFamily = monteBold,
                        color = Color.LightGray,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append("To Access your account")
                }
            }, modifier = Modifier.padding(end = 10.dp))

            val currenanim by rememberLottieComposition(
                spec = LottieCompositionSpec.Asset("robot.json")
            )
            LottieAnimation(
                composition = currenanim,
                iterations = Int.MAX_VALUE,
                contentScale = ContentScale.Crop,
                speed = 1f,
                modifier = Modifier
                    .size(120.dp)
            )

        }
        Spacer(modifier = Modifier.height(30.dp))
        RepeatedLoginButton(
            icon = R.drawable.github,
            description = "Github",
            onClick = {
                githubLogin()
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        RepeatedLoginButton(
            icon = R.drawable.twitter,
            description = "Twitter",
            onClick = {
                twitterLogin()
            }
        )
        Spacer(modifier = Modifier.height(15.dp))
        RepeatedLoginButton(
            icon = R.drawable.google,
            description = "Google",
            onClick = {
                coroutineScope.launch {
                    val signInIntentSender = googleAuthUiClient.signIn()
                    launcher.launch(
                        IntentSenderRequest.Builder(
                            signInIntentSender ?: return@launch
                        ).build()
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = ybc,
                        color = textColor,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black
                    )
                ) {
                    append("Evolve ")
                }
                withStyle(
                    SpanStyle(
                        fontFamily = monteNormal,
                        color = textColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal
                    )
                ) {
                    append("with AI")
                }
            }, modifier = Modifier.padding(end = 10.dp))

        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.MoreHoriz,
                contentDescription = "more",
                tint = textColor.copy(0.45f),
                modifier = Modifier.size(40.dp)
            )

        }


    }

}

fun commonLoginCode(
    provider: OAuthProvider.Builder,
    firebaseAuth: FirebaseAuth,
    context: Context,
    onLogin: () -> Unit = {}
) {
    val pendingResultTask = firebaseAuth.pendingAuthResult
    // There's something already here! Finish the sign-in for your user.
    pendingResultTask?.addOnSuccessListener {
        Log.i("Github", "Started")
    }?.addOnFailureListener {
        Log.i("Github", "Failed")
    }
        ?: firebaseAuth.startActivityForSignInWithProvider(
            context as Activity,
            provider.build()
        )
            .addOnSuccessListener {
                // User is signed in.
                // retrieve the current user
                // navigate to HomePageActivity after successful login
                val intent = Intent(context, MainActivity::class.java)
                // send github user name from MainActivity to HomePageActivity
                Log.i("Github", "Success")
                context.startActivity(intent)
                onLogin()
                Toast.makeText(context, "Login Successfully", Toast.LENGTH_LONG).show()

            }
            .addOnFailureListener {
                // Handle failure.
                Log.i("Github", it.message.toString())
                Toast.makeText(context, "Error : $it", Toast.LENGTH_LONG).show()
            }

}


@Composable
fun RepeatedLoginButton(
    icon: Int,
    description: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 15.dp,
                end = 15.dp,
                top = 7.dp,
                bottom = 7.dp
            )
            .clickable {
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        ),
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = description,
                tint = if (description == "Github") textColor else Color.Unspecified,
                modifier = Modifier
                    .padding(start = 10.dp, end = 0.dp)
                    .size(30.dp)

            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = description,
                    color = textColor,
                    fontSize = 30.sp,
                    fontFamily = monteNormal
                )
            }

        }

    }
}


fun addAccountToManager(
    context: Context,
    email: String,
    name: String,
    photoUrl: String,
    uniqueId: String,
    type: String
) {

    val accountManager = AccountManager.get(context)
    val accountType = context.getString(R.string.accountType)

    val account = Account(email, accountType)
    val userData = Bundle().apply {
        putString("name", name)
        putString("photoUrl", photoUrl)
        putString("uniqueId", uniqueId)
        putString("type", type)
    }
    accountManager.addAccountExplicitly(account, name, userData)
    accountManager.setAuthToken(
        account,
        context.getString(R.string.authTokenType),
        uniqueId
    )
}

