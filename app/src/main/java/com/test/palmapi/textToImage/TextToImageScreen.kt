package com.test.palmapi.textToImage

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.test.palmapi.ImageState
import com.test.palmapi.MainViewModel
import com.test.palmapi.R
import com.test.palmapi.database.chats.ChatMessage
import com.test.palmapi.newChat.ChatCard
import com.test.palmapi.newChat.RepeatedCard
import com.test.palmapi.newChat.convertSpeechToText
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.monteBold
import com.test.palmapi.ui.theme.monteNormal
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import com.test.palmapi.utils.NewChatBottomBar
import com.test.palmapi.utils.NewChatTopBar
import com.test.palmapi.utils.OpenableImage
import com.test.palmapi.utils.byteArrayToBitmap
import com.test.palmapi.utils.saveBitmapImage
import kotlinx.coroutines.launch
import splitties.views.InputType.Companion.text

@Composable
fun TextToImageScreen(
    viewModel: MainViewModel,
    navHostController: NavHostController,
    userPfp: String,
) {
    val imageState by viewModel.imageState.collectAsState()
    var text = remember {
        mutableStateOf(TextFieldValue(""))
    }
    var isRecording = remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val isAnimationVisible = remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            NewChatTopBar(
                modifier = Modifier.then(if (isAnimationVisible.value) Modifier.blur(10.dp) else Modifier),
                viewModel = viewModel,
                navHostController = navHostController,
                onSave = {

                },
                isAIImage = true,
            )
        },
        bottomBar = {
            NewChatBottomBar(
                modifier = Modifier.then(if (isAnimationVisible.value) Modifier.blur(10.dp) else Modifier),
                navController = navHostController,
                viewModel = viewModel,
                text = text.value,
                onTextChange = {
                    text.value = it
                },
                onSpeechToText = {
                    convertSpeechToText(
                        context as Activity,
                        isRecording = isRecording,
                        text = text
                    )
                },
                onClick = {
                    viewModel.imagePrompt.value = text.value.text
                    viewModel.fetchImage(text.value.text)
                    text.value = TextFieldValue("")

                }
            )
        }
    ) {
        println(it)
        Box {
            println(it)
            if (imageState is ImageState.NotStarted) {
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
                            append("Let's Generate ")
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
                            append("AI Powered Images")
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
                        description = "Images are generated using AI"
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
                Column(
                    modifier = Modifier
                        .background(appGradient)
                        .fillMaxSize()
                        .then(if (isAnimationVisible.value) Modifier.blur(10.dp) else Modifier)
                ) {

                    when (imageState) {
                        is ImageState.Loading -> {
                            // Display loading indicator or message
                            isAnimationVisible.value = true
                        }
                        is ImageState.Loaded -> {
                            // Display the loaded image
                            isAnimationVisible.value = false
                            val imageData = (imageState as ImageState.Loaded).imageData
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                OpenableImage(
                                    imageUrl = imageData,
                                    contentScale = ContentScale.Crop,
                                ) {
                                    byteArrayToBitmap(imageData)?.let { it1 ->
                                        saveBitmapImage(
                                            it1,
                                            context = context
                                        )
                                    }
                                    Toast.makeText(
                                        context,
                                        "Image Saved to Gallery",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        is ImageState.Error -> {
                            isAnimationVisible.value = false
                            // Handle error state
                            val error = (imageState as ImageState.Error).exception
                            Text(text = "Error: ${error.message}")
                        }

                        else -> {
                            isAnimationVisible.value = false
                        }
                    }
                }
            }
            if (isAnimationVisible.value) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val currenanim2 by rememberLottieComposition(
                        spec = LottieCompositionSpec.Asset("aiimage.json")
                    )
                    LottieAnimation(
                        composition = currenanim2,
                        iterations = Int.MAX_VALUE,
                        contentScale = ContentScale.Crop,
                        speed = 2.2f,
                        modifier = Modifier
                            .size(200.dp)
                    )
                }
            }

        }
    }
}

