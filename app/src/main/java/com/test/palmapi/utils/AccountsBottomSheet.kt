package com.test.palmapi.utils

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.palmapi.MainViewModel
import com.test.palmapi.R
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.login.GoogleAuthUiClient
import com.test.palmapi.login.ProfileImage
import com.test.palmapi.navigation.Screens
import com.test.palmapi.ui.theme.buttonColor
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import kotlinx.coroutines.launch

@Composable
fun AccountsBottomSheet(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val allAccounts = viewModel.allAccounts.collectAsState(initial = listOf())
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dataStore = UserDatastore(context)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f),
            contentPadding = PaddingValues(top = 20.dp, bottom = 50.dp),
        ) {
            items(allAccounts.value) { account ->
                RepeatedAccountRow(
                    imageUrl = account.photoUrl.toString(),
                    email = account.email,
                    type = account.type,
                    checked = account.isCurrent,
                    onCheckedChange = {
                        coroutineScope.launch {
                            dataStore.saveEmail(account.email)
                            dataStore.saveName(account.firstName + " " + account.lastName)
                            dataStore.savePfp(account.photoUrl.toString())
                            dataStore.saveUID(account.uniqueId)
                        }
                        viewModel.removeCurrentAccount()
                        viewModel.updateCurrentAccount(
                            isCurrent = true,
                            uniqueId = account.uniqueId
                        )
                    },
                ) {
                    coroutineScope.launch {
                        dataStore.saveEmail(account.email)
                        dataStore.saveName(account.firstName + " " + account.lastName)
                        dataStore.savePfp(account.photoUrl.toString())
                        dataStore.saveUID(account.uniqueId)
                    }
                    viewModel.removeCurrentAccount()
                    viewModel.updateCurrentAccount(
                        isCurrent = true,
                        uniqueId = account.uniqueId
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .padding(bottom = 0.dp)
                .offset(y = (-6).dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Manage Accounts",
                    fontSize = 20.sp,
                    color = textColor,
                    modifier = Modifier.padding(start = 10.dp, top = 0.dp)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .padding(bottom = 7.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            AddAccount {
                val googleAuthUiClient by lazy {
                    GoogleAuthUiClient(
                        context = context,
                        oneTapClient = Identity.getSignInClient(context)
                    )
                }
                coroutineScope.launch {
                    googleAuthUiClient.signOut()
                    Firebase.auth.signOut()
                }
                viewModel.removeCurrentAccount()
                navController.navigate(Screens.Login.route)
                Toast.makeText(context, "Signed Out", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun AddAccount(onClick: () -> Unit) {
    Spacer(modifier = Modifier.height(0.dp))
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    top = 80.dp,
                    end = 10.dp,
                    bottom = 0.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Center
        ) {
            Button(
                onClick = { onClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                )
            ) {
                Icon(
                    Icons.Outlined.AddCircleOutline,
                    contentDescription = "Github",
                    tint = textColor,
                    modifier = Modifier
                        .padding(start = 0.dp, end = 0.dp)
                        .size(20.dp)

                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = "Add Account",
                    color = textColor,
                    fontSize = 15.sp,
                    fontFamily = monteSB
                )
            }
        }
    }
}

@Composable
fun RepeatedAccountRow(
    imageUrl: String,
    email: String,
    type: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 10.dp,
                top = 0.dp,
                end = 10.dp,
                bottom = 0.dp
            )
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (type) {
            "github" -> Icon(
                painter = painterResource(id = R.drawable.github),
                contentDescription = "Github",
                tint = textColor,
                modifier = Modifier
                    .padding(start = 10.dp, end = 0.dp)
                    .size(20.dp)

            )

            "google" -> Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = 10.dp, end = 0.dp)
                    .size(20.dp)

            )

            "twitter" -> Icon(
                painter = painterResource(id = R.drawable.twitter),
                contentDescription = "Twitter",
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = 10.dp, end = 0.dp)
                    .size(20.dp)

            )
        }
        Spacer(modifier = Modifier.width(15.dp))
        ProfileImage(
            imageUrl = imageUrl,
            modifier = Modifier
                .size(30.dp)
                .border(
                    width = 1.dp,
                    color = textColor.copy(0.5f),
                    shape = CircleShape
                )
                .padding(3.dp)
                .clip(CircleShape),
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = email,
            color = textColor,
            fontSize = 12.sp,
            fontFamily = monteSB
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            RadioButton(
                selected = checked,
                onClick = {
                    onCheckedChange(checked)
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = textColor,
                    unselectedColor = Color.Gray
                )
            )

        }


    }
}