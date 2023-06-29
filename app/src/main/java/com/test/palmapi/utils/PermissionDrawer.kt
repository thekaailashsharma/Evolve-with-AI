package com.test.palmapi.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.monteNormal
import com.test.palmapi.ui.theme.textColor


@ExperimentalPermissionsApi
@ExperimentalComposeUiApi
@ExperimentalMaterial3Api
@Composable
fun PermissionDrawer(
    drawerState: BottomSheetScaffoldState,
    model: Any,
    permissionState: MultiplePermissionsState,
    rationaleText: String,
    gesturesEnabled: Boolean = false,
    withoutRationaleText: String,
    size: Dp = 60.dp,
    content: @Composable (PaddingValues) -> Unit,
) {


    BottomSheetScaffold(
        sheetContent = {
            PermissionDrawerContent(
                permissionState = permissionState,
                model = model,
                rationaleText = rationaleText,
                withoutRationaleText = withoutRationaleText,
                size = size
            )
        },
        scaffoldState = drawerState,
        sheetContainerColor = CardColor.copy(0.95f),
        sheetSwipeEnabled = gesturesEnabled,
        content = content
    )
}


@ExperimentalPermissionsApi
@ExperimentalMaterial3Api
@Composable
fun PermissionDrawerContent(
    size: Dp,
    model: Any,
    rationaleText: String,
    withoutRationaleText: String,
    permissionState: MultiplePermissionsState,
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)
            .padding(
                top = 15.dp,
                bottom = 10.dp,
                start = 25.dp,
                end = 25.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            AsyncImage(
                model = model,
                contentDescription = null,
                modifier = Modifier.size(size),
                contentScale = ContentScale.FillWidth,
            )
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp),
                tint = textColor
            )
            Image(
                painter = painterResource(com.test.palmapi.R.drawable.appicon),
                contentDescription = null,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.FillWidth
            )
        }
        if (permissionState.shouldShowRationale)
            Text(
                text = rationaleText,
                textAlign = TextAlign.Center,
                color = textColor,
                fontFamily = monteNormal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        else
            Text(
                text = withoutRationaleText,
                textAlign = TextAlign.Center,
                color = textColor,
                fontFamily = monteNormal,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )

        Button(
            modifier = Modifier.padding(top = 5.dp),
            onClick = {
                if (permissionState.shouldShowRationale) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + context.packageName)
                    context.startActivity(intent)
                } else {
                    permissionState.launchMultiplePermissionRequest()
                }
            },
            shape = CircleShape
        ) {
            if (permissionState.shouldShowRationale)
                Text(
                    text = "Settings",
                    color = Color.White,
                    fontFamily = monteNormal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            else
                Text(
                    text = "Grant Permission",
                    color = Color.White,
                    fontFamily = monteNormal,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
        }
    }
}
