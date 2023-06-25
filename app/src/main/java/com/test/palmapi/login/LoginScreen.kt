package com.test.palmapi.login

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.palmapi.MainActivity
import com.test.palmapi.R
import com.test.palmapi.navigation.Screens
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.appBackground
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.monteBold
import com.test.palmapi.ui.theme.monteNormal
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import com.test.palmapi.ui.theme.ybc
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun LoginScreen(navHostController: NavHostController) {
    val firebaseAuth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val githubLogin = {
        val provider = OAuthProvider.newBuilder("github.com")
        commonLoginCode(provider, firebaseAuth, context)
    }
    val twitterLogin = {
        val provider = OAuthProvider.newBuilder("twitter.com")
        commonLoginCode(provider, firebaseAuth, context)
    }
    val token = stringResource(R.string.default_web_client_id)
    var user by remember { mutableStateOf(Firebase.auth.currentUser) }
    LaunchedEffect(key1 = user) {
        if (user != null) {
            navHostController.popBackStack()
            navHostController.navigate(Screens.NewChat.route)
        }
    }
    val launcher = rememberFirebaseAuthLauncher(
        onAuthComplete = { result ->
            user = result.user
        },
        onAuthError = {
            user = null
        }
    )
    val googleLogin = {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(token)
                .requestEmail().requestProfile()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        googleSignInClient.signOut()
        launcher.launch(googleSignInClient.signInIntent)

    }


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
                googleLogin()
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
    context: Context
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

@Composable
fun rememberFirebaseAuthLauncher(
    onAuthComplete: (AuthResult) -> Unit,
    onAuthError: (ApiException) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val scope = rememberCoroutineScope()
    return rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
            scope.launch {
                val authResult = Firebase.auth.signInWithCredential(credential).await()
                onAuthComplete(authResult)
            }
        } catch (e: ApiException) {
            onAuthError(e)
        }
    }
}