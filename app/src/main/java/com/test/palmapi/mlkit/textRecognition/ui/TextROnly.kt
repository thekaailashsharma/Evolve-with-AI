package com.test.palmapi.mlkit.textRecognition.ui

import android.Manifest
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.test.palmapi.R
import com.test.palmapi.mlkit.BottomBox
import com.test.palmapi.mlkit.Models
import com.test.palmapi.utils.PermissionDrawer

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun TextROnly() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
        )
    )
    var isTextClicked by remember { mutableStateOf(false) }
    val permissionDrawerState = rememberBottomSheetScaffoldState(
        if (permissionState.allPermissionsGranted) SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = true
        )
        else SheetState(
            initialValue = SheetValue.Expanded,
            skipPartiallyExpanded = true
        )
    )
    val gesturesEnabled by remember { derivedStateOf { permissionState.allPermissionsGranted } }
    Log.i("GesturesEnabled", gesturesEnabled.toString())
    PermissionDrawer(
        drawerState = permissionDrawerState,
        model = R.drawable.camera,
        permissionState = permissionState,
        rationaleText = "To continue, allow " +
                "${stringResource(id = R.string.app_name)} " +
                "to access your device's mlkit Tap Settings > Permission, and turn " +
                "\"Access Camera On\" on.",
        withoutRationaleText = "Camera permission required for this feature to be available." +
                " Please grant the permission.",
        gesturesEnabled = gesturesEnabled,
    ) {
        val extractedText = remember { mutableStateOf("") }
        Log.i("ExtractedText", extractedText.value)
        Box(modifier = Modifier.fillMaxSize()) {
            TextRecognitionView(
                context = context,
                lifecycleOwner = lifecycleOwner,
                extractedText = extractedText
            )
        }
        BottomBox(
            extractedText.value,
            isTextClicked,
            model = Models.TEXT_RECOGNITION.name,
            changeModel = {}
        ) {
            isTextClicked = !isTextClicked
        }

    }
}