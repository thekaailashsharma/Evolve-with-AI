package com.test.palmapi.services

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.provider.Settings
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.test.palmapi.bottombar.BottomBar
import com.test.palmapi.navigation.Screens
import com.test.palmapi.services.TypesOfService.Accessibility
import com.test.palmapi.services.TypesOfService.BarCodeScanner
import com.test.palmapi.services.TypesOfService.Keyboard
import com.test.palmapi.services.TypesOfService.TextRecognition
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import com.test.palmapi.utils.updateTransitionData
import splitties.systemservices.inputMethodManager

data class Services(
    val name: TypesOfService,
    val desc: String,
    val anim: LottieCompositionSpec
)

enum class TypesOfService {
    Keyboard, Accessibility, TextRecognition, BarCodeScanner
}

enum class CardState { Front, Back }

val items1 = listOf(
    Services(
        name = Keyboard,
        desc = "Use our keyboard with inbuilt AI to get suggestions while typing!",
        anim = LottieCompositionSpec.Asset("keyboard.json")
    ),
    Services(
        name = Accessibility,
        desc = "Use Accessibility feature and access AI from anywhere!",
        anim = LottieCompositionSpec.Asset("accesibility.json")
    ),
)
val items2 = listOf(
    Services(
        name = TextRecognition,
        desc = "Use our text recognition feature to recognize text from images !",
        anim = LottieCompositionSpec.Asset("textr.json")
    ),
    Services(
        name = BarCodeScanner,
        desc = "Use our barcode scanner to scan barcodes from images !",
        anim = LottieCompositionSpec.Asset("qrcode.json")
    )
)

@Composable
fun OurServices(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        println(it)
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(appGradient),
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Our Services",
                            color = textColor,
                            fontSize = 30.sp,
                            fontFamily = monteSB,
                            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                        )
                        Text(
                            text = "Check out our services below!",
                            color = textColor,
                            fontSize = 15.sp,
                            fontFamily = monteSB,
                            modifier = Modifier.padding(top = 0.dp, bottom = 20.dp)
                        )

                    }
                }
            }
            item {
                TwoServicesCard(
                    navController = navController,
                    spec1 = items1[0].anim,
                    serviceName1 = items1[0].name,
                    serviceDesc1 = items1[0].desc,
                    spec2 = items1[1].anim,
                    serviceName2 = items1[1].name,
                    serviceDesc2 = items1[1].desc,
                )
            }
            item {
                TwoServicesCard(
                    navController = navController,
                    spec1 = items2[0].anim,
                    serviceName1 = items2[0].name,
                    serviceDesc1 = items2[0].desc,
                    spec2 = items2[1].anim,
                    serviceName2 = items2[1].name,
                    serviceDesc2 = items2[1].desc,
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp)
                ) {
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        text = "Your Confidentiality",
                        color = textColor.copy(0.75f),
                        fontSize = 26.sp,
                        fontFamily = monteSB,
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Our Priority Always !",
                        color = textColor.copy(0.75f),
                        fontSize = 19.sp,
                        fontFamily = monteSB,
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun TwoServicesCard(
    navController: NavHostController,
    spec1: LottieCompositionSpec,
    serviceName1: TypesOfService,
    serviceDesc1: String,
    spec2: LottieCompositionSpec,
    serviceName2: TypesOfService,
    serviceDesc2: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(1f),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ServicesCard(
                navController = navController,
                spec = spec1,
                serviceName = serviceName1,
                serviceDesc = serviceDesc1
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ServicesCard(
                navController = navController,
                spec = spec2,
                serviceName = serviceName2,
                serviceDesc = serviceDesc2
            )
        }
    }

}

@Composable
fun ServicesCard(
    navController: NavHostController,
    spec: LottieCompositionSpec,
    serviceName: TypesOfService,
    serviceDesc: String,
) {
    val ctx = LocalContext.current
    var rotated by remember { mutableStateOf(false) }
    val transitionData = updateTransitionData(
        if (rotated) CardState.Back else CardState.Front
    )
    Card(
        modifier = Modifier
            .size(400.dp, 300.dp)
            .padding(
                start = 7.dp,
                end = 7.dp,
                top = 7.dp,
                bottom = 7.dp
            )
            .graphicsLayer {
                rotationY = transitionData.rotation
                cameraDistance = 8 * density
            }
            .clickable {
                rotated = !rotated
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(appGradient)
                .graphicsLayer {
                    alpha =
                        if (rotated) transitionData.animateBack else transitionData.animateFront
                    rotationY = transitionData.rotation
                }

        ) {

            Text(
                text = serviceName.name,
                color = textColor,
                fontSize = 12.sp,
                fontFamily = monteSB,
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp)
            )
            if (!rotated) {
                val currenanim by rememberLottieComposition(
                    spec = spec
                )
                LottieAnimation(
                    composition = currenanim,
                    iterations = Int.MAX_VALUE,
                    contentScale = ContentScale.Crop,
                    speed = 0.5f,
                    modifier = Modifier
                        .size(100.dp)
                )
                Spacer(modifier = Modifier.height(22.dp))
                Text(
                    text = serviceDesc,
                    color = textColor,
                    fontSize = 10.sp,
                    fontFamily = monteSB,
                    modifier = Modifier.padding(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = 10.dp,
                        end = 10.dp
                    ),
                    softWrap = true
                )
            } else {
                when (serviceName) {
                    Keyboard -> {
                        Text(
                            text = "Type anything inside (( )) to get answers from AI..",
                            fontSize = 10.sp,
                            softWrap = true,
                            color = Color.Green,
                            modifier = Modifier.padding(horizontal = 7.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (inputMethodManager.enabledInputMethodList.any {
                                it.packageName == "com.test.palmapi"
                            }) {
                            Text(
                                text = "Evolve AI Keyboard is enabled!",
                                fontSize = 10.sp,
                                softWrap = true,
                                color = Color.Green
                            )
                        } else {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp),
                                onClick = {
                                    ctx.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
                                }
                            ) {
                                Text(
                                    text = "Enable Evolve AI Keyboard",
                                    fontSize = 10.sp,
                                    softWrap = true
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Button(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp), onClick = {
                            inputMethodManager.showInputMethodPicker()
                        }) {
                            Text(
                                text = "Select Keyboard",
                                fontSize = 10.sp,
                                softWrap = true
                            )
                        }


                    }

                    Accessibility -> {
                        Text(
                            text = "Type anything inside {{ }} to get answers from AI..",
                            fontSize = 10.sp,
                            softWrap = true,
                            color = Color.Green,
                            modifier = Modifier.padding(horizontal = 7.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        if (isAccessibilityPermissionGranted(ctx.contentResolver)) {
                            Text(
                                text = "Accessibility is enabled!",
                                fontSize = 10.sp,
                                softWrap = true,
                                color = Color.Green
                            )
                        } else {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp),
                                onClick = {
                                    openAccessibilitySettings(ctx)
                                }
                            ) {
                                Text(
                                    text = "Enable Accessibility",
                                    fontSize = 10.sp,
                                    softWrap = true
                                )
                            }
                        }


                    }

                    TextRecognition -> {
                        navController.navigate(Screens.TextROnly.route)
                    }

                    BarCodeScanner -> {
                        navController.navigate(Screens.BarCodeOnly.route)
                    }
                }
            }

        }
    }
}

fun isAccessibilityPermissionGranted(contentResolver: ContentResolver): Boolean {
    val accessibilityEnabled =
        Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED, 0)
    return accessibilityEnabled == 1
}


fun openAccessibilitySettings(context: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(context, intent, null)
}

