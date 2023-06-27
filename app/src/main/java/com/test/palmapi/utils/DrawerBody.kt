package com.test.palmapi.utils

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.HelpCenter
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.palmapi.R
import com.test.palmapi.login.ProfileImage
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.buttonColor
import com.test.palmapi.ui.theme.isDarkThemEnabled
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.openDeviceThemeSettings
import com.test.palmapi.ui.theme.textColor
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    state: DrawerState,
    photoUrl: String,
    displayName: String,
    email: String,
) {
    val context = LocalContext.current
    val modalSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = false
        )
    )

    BottomSheetScaffold(
        sheetContent = {

        },
        sheetContainerColor = CardColor.copy(0.95f),
        scaffoldState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetPeekHeight = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(if (state.isOpen) 0.65f else 0.0f)
                .background(appGradient)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 35.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileImage(
                        imageUrl = photoUrl,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(3.dp)
                            .clip(CircleShape),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = displayName.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            }.substringBefore(" "),
                            color = textColor,
                            fontSize = 20.sp,
                            fontFamily = monteSB
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = "Star",
                            tint = textColor,
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                    Log.i("Email", email)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "@ $email",
                        color = textColor,
                        fontSize = 9.sp,
                        fontFamily = monteSB
                    )

                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            DrawerCard(
                image = Icons.Outlined.Settings,
                description = "Settings"
            )
            Spacer(modifier = Modifier.height(20.dp))
            DrawerCard(
                image = Icons.Outlined.HelpCenter,
                description = "Help Center"
            )
            Spacer(modifier = Modifier.height(20.dp))
            DrawerCard(
                image = Icons.Outlined.Feedback,
                description = "Send Feedback"
            )
            Spacer(modifier = Modifier.height(20.dp))
            DrawerCard(
                image = Icons.Outlined.StarRate,
                description = "Rate Us"
            )
            Spacer(modifier = Modifier.height(20.dp))
            DrawerCard(
                image = Icons.Outlined.Logout,
                description = "Logout"
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SwitchWithLabel(
                    state = isDarkThemEnabled,
                    onStateChange = {
                        openDeviceThemeSettings(context)
                        Toast.makeText(
                            context,
                            "Change System theme to change the app theme",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                )


            }
        }
    }
}

@Composable
private fun SwitchWithLabel(
    state: Boolean,
    onStateChange: (Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = buttonColor,
        ),
        shape = RoundedCornerShape(30.dp),
    ) {
        Row(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    role = Role.Switch,
                    onClick = {
                        onStateChange(!state)
                    }
                )
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {

            Text(
                text = if (state) "Dark Mode" else "Light Mode",
                color = textColor,
                fontSize = 18.sp,
                fontFamily = monteSB,
                modifier = Modifier.padding(start = 20.dp)
            )
            val icon: (@Composable () -> Unit) = if (state) {
                {
                    Icon(
                        painter = painterResource(id = R.drawable.moon),
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                        tint = textColor
                    )
                }
            } else {
                {
                    Icon(
                        painter = painterResource(id = R.drawable.sun),
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                        tint = textColor
                    )


                }
            }

            Switch(
                modifier = Modifier
                    .semantics { contentDescription = "Demo with icon" }
                    .fillMaxWidth(),
                checked = state,
                onCheckedChange = {
                    onStateChange(!state)
                },
                thumbContent = icon
            )

        }
    }
}

@Composable
fun DrawerCard(
    image: ImageVector,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp)
    ) {
        Icon(
            imageVector = image,
            contentDescription = description,
            tint = textColor,
            modifier = Modifier
                .size(30.dp)
                .padding(end = 5.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = description,
            color = textColor,
            fontSize = 18.sp,
            fontFamily = monteSB,
            textAlign = TextAlign.Start
        )
    }

}