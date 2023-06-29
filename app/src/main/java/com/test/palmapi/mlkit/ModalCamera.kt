package com.test.palmapi.mlkit

import android.Manifest
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChangeCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.test.palmapi.R
import com.test.palmapi.mlkit.barcode.ui.CodeScannerView
import com.test.palmapi.mlkit.textRecognition.ui.TextRecognitionView
import com.test.palmapi.ui.theme.monteNormal
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import com.test.palmapi.utils.Grapple
import com.test.palmapi.utils.PermissionDrawer

enum class Models {
    TEXT_RECOGNITION,
    BARCODE_SCANNER
}

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun ModalCamera() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
        )
    )
    var isTextClicked by remember { mutableStateOf(false) }
    var whichML by remember { mutableStateOf(Models.TEXT_RECOGNITION) }
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
            if (!isTextClicked) {
                if (whichML == Models.TEXT_RECOGNITION) {
                    TextRecognitionView(
                        context = context,
                        lifecycleOwner = lifecycleOwner,
                        extractedText = extractedText
                    )
                } else if (whichML == Models.BARCODE_SCANNER) {
                    CodeScannerView(code = extractedText)
                }
            }

            BottomBox(extractedText.value, isTextClicked, model = whichML.name, changeModel = {
                whichML =
                    if (whichML == Models.TEXT_RECOGNITION)
                        Models.BARCODE_SCANNER else Models.TEXT_RECOGNITION
                extractedText.value = ""
            }) {
                isTextClicked = !isTextClicked
            }


        }

    }
}

@Composable
fun BottomBox(
    code: String,
    isTextClicked: Boolean = false,
    model: String = "Text Recognition",
    changeModel: () -> Unit,
    onTextClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(if (isTextClicked) 0.85f else 0.4f),
            colors = CardDefaults.cardColors(Color(0xFF353739).copy(0.55f)),
            shape = RoundedCornerShape(topStart = 7.dp, topEnd = 7.dp),
            elevation = CardDefaults.elevatedCardElevation(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Grapple(
                        modifier = Modifier
                            .padding(bottom = 0.dp)
                            .requiredHeight(15.dp)
                            .requiredWidth(55.dp)
                            .alpha(0.22f), color = textColor
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(start = 13.dp)
                    ) {
                        Text(
                            text = model,
                            color = textColor,
                            fontSize = 25.sp,
                            fontFamily = monteSB,

                            )
                        if (isTextClicked) {
                            Text(
                                text = "Long click select text",
                                color = textColor,
                                fontFamily = monteSB,
                                fontSize = 13.sp
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 7.dp, top = 0.dp)
                            .clickable { changeModel() },
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ChangeCircle,
                            contentDescription = "",
                            tint = textColor
                        )

                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 7.dp, end = 7.dp, top = 10.dp, bottom = 80.dp
                        )
                        .verticalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (isTextClicked) {
                        SelectionContainer {
                            Text(
                                text = code,
                                color = textColor,
                                fontSize = 16.sp,
                                fontFamily = monteNormal,
                                softWrap = true
                            )
                        }
                    } else {
                        Text(
                            text = code,
                            color = textColor,
                            fontSize = 16.sp,
                            fontFamily = monteNormal,
                            softWrap = true
                        )
                    }
                }

            }

        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    onTextClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF353739).copy(0.55f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 55.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = if (isTextClicked) "Capture Again" else "Select Text",
                    color = textColor,
                    fontFamily = monteSB,
                    fontSize = 13.sp
                )
            }
        }


    }
}






