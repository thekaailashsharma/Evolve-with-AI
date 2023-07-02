package com.test.palmapi.utils


import android.content.Intent
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.sharp.ArrowBackIos
import androidx.compose.material.icons.sharp.Bookmark
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.test.palmapi.MainViewModel
import com.test.palmapi.R
import com.test.palmapi.login.ProfileImage
import com.test.palmapi.navigation.Screens
import com.test.palmapi.ui.theme.COLLAPSED_TOP_BAR_HEIGHT
import com.test.palmapi.ui.theme.EXPANDED_TOP_BAR_HEIGHT
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.buttonColor
import com.test.palmapi.ui.theme.githubColors
import com.test.palmapi.ui.theme.googleColors
import com.test.palmapi.ui.theme.isDarkThemEnabled
import com.test.palmapi.ui.theme.monteNormal
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import com.test.palmapi.ui.theme.twitterColors
import com.test.palmapi.ui.theme.ybc


@Composable
fun NewChatTopBar(
    viewModel: MainViewModel,
    onSave: () -> Unit = {},
    navHostController: NavHostController
) {
    val texts = listOf("New Chat")
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDarkThemEnabled) Color(0xFF081427) else Color(0xFFf0f5fc))
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 16.dp
            ),
    ) {
        Icon(
            imageVector = Icons.Sharp.ArrowBackIos,
            contentDescription = "Back",
            tint = textColor,
            modifier = Modifier
                .size(24.dp)
                .clickable {
                    navHostController.navigateUp()
                }
        )

        TypewriterText(
            texts = if (viewModel.isSaved.value) listOf(viewModel.savedName.value) else texts,
            text = if (viewModel.isSaved.value) viewModel.savedName.value else "",
            modifier = Modifier.padding(start = if (viewModel.isSaved.value) 5.dp else 30.dp),
            delay = 160L
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Sharp.Bookmark,
                contentDescription = "Bookmark",
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        onSave()
                    }
            )
            Spacer(modifier = Modifier.width(15.dp))
            Icon(
                imageVector = Icons.Outlined.Feedback,
                contentDescription = "Share",
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("kailashps.1011@gmail.com"))
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback:Evolve with AI")
                        intent.setType("message/rfc822")
                        context.startActivity(
                            Intent.createChooser(
                                intent,
                                "Please share your feedback"
                            )
                        )
                    }
            )


        }

    }
}

@Composable
fun ExpandedTopBarHomeScreen(
    navController: NavHostController,
    imageUrl: String,
    textValue: String,
    type: String,
    onTextChange: (String) -> Unit,
    openDrawerClick: () -> Unit = {},
    closeDrawerClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .height(EXPANDED_TOP_BAR_HEIGHT - COLLAPSED_TOP_BAR_HEIGHT)
            .background(appGradient)
            .clickable {
                closeDrawerClick()
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage(
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .size(100.dp)
                        .border(
                            brush = Brush.verticalGradient(
                                colors = when (type) {
                                    "google" -> {
                                        googleColors
                                    }

                                    "twitter" -> {
                                        twitterColors
                                    }

                                    "github" -> {
                                        githubColors
                                    }

                                    else -> {
                                        listOf(Color.White, Color.White)
                                    }
                                }
                            ), shape = CircleShape, width = 1.dp
                        )
                        .clip(CircleShape)
                        .clickable {
                            openDrawerClick()
                        },
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Welcome to ",
                    color = textColor,
                    fontSize = 20.sp,
                    fontFamily = monteSB
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Evolve AI",
                    color = textColor,
                    fontSize = 26.sp,
                    fontFamily = monteSB,
                    textAlign = TextAlign.Center
                )
            }

        }

        Spacer(modifier = Modifier.height(15.dp))
        val containerColor = if (isDarkThemEnabled) Color(0xFF162130)
        else Color(0xFFe6e9f1)
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp)
                .clickable {
                    navController.navigate(Screens.NewChat.route)
                },
            value = textValue,
            enabled = false,
            onValueChange = {

            },
            label = {
                Text(text = "Ask me anything...", color = textColor)
            },
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.photo),
                        contentDescription = "Photos",
                        tint = textColor,
                        modifier = Modifier
                            .size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.mic),
                        contentDescription = "Microphone",
                        tint = textColor,
                        modifier = Modifier
                            .size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))


                }
            },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
            )
        )

    }
}

@Composable
fun CollapsedTopBarHomeScreen(
    type: String,
    imageUrl: String,
    isCollapsed: Boolean,
    scroll: LazyListState
) {
    AnimatedVisibility(
        visible = isCollapsed,
        enter = expandVertically(animationSpec = tween(durationMillis = 300)) + fadeIn(),
        exit = shrinkVertically(animationSpec = tween(durationMillis = 300)) + fadeOut()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(appGradient)
                .height(COLLAPSED_TOP_BAR_HEIGHT)
                .padding(vertical = 10.dp, horizontal = 20.dp)
                .graphicsLayer {
                    translationY = -scroll.firstVisibleItemIndex.toFloat() / 2f
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
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
            ProfileImage(
                imageUrl = imageUrl,
                modifier = Modifier
                    .size(50.dp)
                    .border(
                        brush = Brush.verticalGradient(
                            colors = when (type) {
                                "google" -> {
                                    googleColors
                                }

                                "twitter" -> {
                                    twitterColors
                                }

                                "github" -> {
                                    githubColors
                                }

                                else -> {
                                    listOf(Color.White, Color.White)
                                }
                            }
                        ), shape = CircleShape, width = 1.dp
                    )
                    .clip(CircleShape),
            )

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewChatBottomBar(
    navController: NavHostController,
    viewModel: MainViewModel,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onSpeechToText: () -> Unit,
    onClick: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyboardOpen by keyboardAsState()
    Log.i("Messages", viewModel.listOfMessages.value.toString())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDarkThemEnabled) Color(0xFF081427) else Color(0xFFf0f5fc))
            .padding(
                start = 10.dp,
                end = 10.dp,
                top = 5.dp,
                bottom = 16.dp
            ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isKeyboardOpen == Keyboard.Closed) {
            Icon(
                painter = painterResource(id = R.drawable.sparkle),
                contentDescription = "Share",
                tint = textColor,
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 10.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.photo),
                contentDescription = "Photos",
                tint = textColor,
                modifier = Modifier
                    .size(35.dp)
                    .padding(end = 10.dp)
                    .clickable {
                        navController.navigate(Screens.ModalCamera.route)
                    }
            )
            Icon(
                painter = painterResource(id = R.drawable.mic),
                contentDescription = "Microphone",
                tint = textColor,
                modifier = Modifier
                    .size(39.dp)
                    .padding(end = 10.dp)
                    .clickable {
                        onSpeechToText()
                    }
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center
        ) {
            val containerColor = if (isDarkThemEnabled) Color(0xFF162130)
            else Color(0xFFe6e9f1)
            OutlinedTextField(
                value = text,
                onValueChange = {
                    onTextChange(it)
                },
                label = {
                    Text(text = "Ask me anything", color = textColor)
                },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    disabledContainerColor = containerColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                )
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                painter = painterResource(id = R.drawable.send),
                contentDescription = "Send",
                tint = textColor,
                modifier = Modifier
                    .size(39.dp)
                    .padding(end = 10.dp)
                    .clickable {
                        onClick()
                        keyboardController?.hide()
                    }
            )
        }


    }
}

enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = android.graphics.Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

@Composable
fun BottomSheetContent(viewModel: MainViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxHeight(0.25f)
            .fillMaxWidth()
            .padding(start = 17.dp, end = 17.dp, bottom = 10.dp)
    ) {
        var text by remember { mutableStateOf(TextFieldValue("")) }
        LaunchedEffect(key1 = Unit) {
            if (viewModel.isSaved.value) {
                text = TextFieldValue(viewModel.savedName.value)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Save Message",
                color = textColor,
                fontSize = 20.sp,
                fontFamily = monteSB
            )

        }
        val context = LocalContext.current
        val containerColor = if (isDarkThemEnabled) Color(0xFF162130)
        else Color(0xFFe6e9f1)
        OutlinedTextField(
            value = text,
            onValueChange = {

            if (it.text.length <= 20) {
                    text = it
                } else {
                    Toast.makeText(
                        context,
                        "The name can't be longer than 20 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            label = {
                Text(
                    text = "Save as ...",
                    color = textColor
                )
            },
            shape = RoundedCornerShape(30.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = containerColor,
                unfocusedContainerColor = containerColor,
                disabledContainerColor = containerColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
            ),
            modifier = Modifier.fillMaxWidth()

        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                onClick = {
                    if (viewModel.isSaved.value) {
                        viewModel.saveChatMessage(text.text, viewModel.savedName.value, false)
                        navController.navigateUp()
                    } else {
                        viewModel.saveChatMessage(text.text, isPined = false)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                )
            ) {
                Text(text = "Save", color = textColor, fontFamily = monteSB, fontSize = 20.sp)
            }
        }


    }
}


