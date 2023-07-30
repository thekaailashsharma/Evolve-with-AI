package com.test.palmapi.prompts

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.test.palmapi.MainViewModel
import com.test.palmapi.R
import com.test.palmapi.database.chats.ChatMessage
import com.test.palmapi.imeService.IMEService
import com.test.palmapi.newChat.ChatCard
import com.test.palmapi.newChat.RepeatedCard
import com.test.palmapi.newChat.convertSpeechToText
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.monteNormal
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import com.test.palmapi.utils.BottomSheetContent
import com.test.palmapi.utils.NewChatBottomBar
import com.test.palmapi.utils.NewChatTopBar
import com.test.palmapi.utils.PermissionDrawer
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun PromptScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    photoUrl: String,
    uid: String,
    prompt: String,
    emotion: String
) {
    Log.i("Promptttt", Uri.parse(prompt).toString())
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    BackHandler {
        viewModel.savedName.value = ""
        viewModel.isSaved.value = false
        navController.navigateUp()
    }
    var text = remember {
        mutableStateOf(TextFieldValue(prompt))
    }

    var isRecording = remember {
        mutableStateOf(false)
    }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val result by viewModel.savedMessages(viewModel.savedName.value)
        .collectAsState(initial = listOf())
    LaunchedEffect(key1 = result) {
        try {
            if (result.isNotEmpty()) {
                lazyListState.animateScrollToItem(result.size - 1)
            }
        } catch (e: Exception) {
            Log.i("Message", e.toString())
        }
    }
    Log.i("ProfileImage", photoUrl)
    val modalSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = false
        )
    )

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.RECORD_AUDIO,
        )
    )
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
        model = R.drawable.mic1,
        permissionState = permissionState,
        rationaleText = "To continue, allow " +
                "${stringResource(id = R.string.app_name)} " +
                "to access your speech to text Tap Settings > Permission, and turn " +
                "\"Access Microphone On\" on.",
        withoutRationaleText = "Microphone permission required for this feature to be available." +
                " Please grant the permission.",
        gesturesEnabled = gesturesEnabled,
    ) {
        BottomSheetScaffold(
            sheetContent = {
                BottomSheetContent(viewModel, navController)
            },
            sheetContainerColor = CardColor.copy(0.95f),
            scaffoldState = modalSheetState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetPeekHeight = 0.dp,
        ) {
            Scaffold(
                topBar = {
                    NewChatTopBar(
                        viewModel = viewModel,
                        navHostController = navController,
                        onSave = {
                            coroutineScope.launch {
                                modalSheetState.bottomSheetState.expand()
                            }
                        }
                    )
                },
                bottomBar = {
                    NewChatBottomBar(
                        navController = navController,
                        viewModel = viewModel,
                        text = text.value,
                        onTextChange = {
                            text.value = it
                        },
                        onSpeechToText = {
                            convertSpeechToText(
                                context as Activity,
                                isRecording = isRecording,
                                text = text,
                            )
                        },
                        onClick = {
                            viewModel.insertChat(
                                ChatMessage(
                                    message = text.value.text,
                                    time = System.currentTimeMillis(),
                                    isUser = true,
                                    uID = uid
                                )
                            )
                            viewModel.message.value = text.value.text
                            viewModel.getApiData()
                            coroutineScope.launch {
                                try {
                                    if (result.isNotEmpty()) {
                                        lazyListState.animateScrollToItem(result.size - 1)
                                    }
                                } catch (e: Exception) {
                                    Log.i("Message", e.toString())
                                }
                            }
                            text.value = TextFieldValue("")

                        }
                    )
                }
            ) {
                println(it)
                Box {
                    if (result.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .background(appGradient)
                                .fillMaxSize()
                                .padding(top = 150.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        fontFamily = monteNormal,
                                        color = textColor,
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                ) {
                                    append("Hello, Ask Me")
                                }
                                append("\n")
                                withStyle(
                                    SpanStyle(
                                        fontFamily = monteNormal,
                                        color = textColor,
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                ) {
                                    append("Anything ...")
                                }
                            })
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "I'm your assistant.",
                                color = textColor,
                                fontFamily = monteSB
                            )
                            Spacer(modifier = Modifier.height(15.dp))
                            RepeatedCard(
                                icon = R.drawable.beta,
                                description = "I'm still under development"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            RepeatedCard(
                                icon = R.drawable.remember,
                                description = "I can remember our conversations"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            RepeatedCard(
                                icon = R.drawable.privacy,
                                description = "Your Privacy is completely secured"
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            RepeatedCard(
                                icon = R.drawable.tip,
                                description = "Long Press on Chat to see options"
                            )
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
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .background(appGradient)
                                .fillMaxSize()
                                .padding(top = 30.dp),
                            contentPadding = PaddingValues(top = 40.dp, bottom = 130.dp),
                            state = lazyListState,
                        ) {
                            items(result) { message ->
                                ChatCard(
                                    text = message.message ?: "",
                                    isUser = message.isUser,
                                    viewModel = viewModel,
                                    imageUrl = photoUrl,
                                    time = message.time,
                                )
                            }

                        }
                    }
                    if (isRecording.value) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            val currenanim by rememberLottieComposition(
                                spec = LottieCompositionSpec.Asset("voice.json")
                            )
                            LottieAnimation(
                                composition = currenanim,
                                iterations = Int.MAX_VALUE,
                                contentScale = ContentScale.Crop,
                                speed = 0.5f,
                                modifier = Modifier
                                    .size(150.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
