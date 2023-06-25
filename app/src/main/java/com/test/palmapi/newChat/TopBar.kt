package com.test.palmapi.newChat

import android.graphics.Rect
import android.util.Log
import android.view.ViewTreeObserver
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.InsertPhoto
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.test.palmapi.MainViewModel
import com.test.palmapi.R
import com.test.palmapi.dto.Candidate
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.textColor

@Composable
fun NewChatTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 16.dp
            )
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBackIos,
            contentDescription = "Back",
            tint = textColor,
            modifier = Modifier.size(24.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = "Share",
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )


        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewChatBottomBar(
    viewModel: MainViewModel,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    onClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyboardOpen by keyboardAsState()
    Log.i("Messages", viewModel.listOfMessages.value.toString())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isSystemInDarkTheme()) Color(0xFF081427) else Color(0xFFf0f5fc))
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
            )
            Icon(
                painter = painterResource(id = R.drawable.mic),
                contentDescription = "Microphone",
                tint = textColor,
                modifier = Modifier
                    .size(39.dp)
                    .padding(end = 10.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center
        ) {
            val containerColor = if (isSystemInDarkTheme()) Color(0xFF162130)
            else Color(0xFFe6e9f1)
            OutlinedTextField(
                value = text,
                onValueChange = {
                    onTextChange(it)
                },
                label = {
                    Text(text = "Ask me anything", color = textColor)
                },
                shape = RoundedCornerShape(100.dp),
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
            Log.i("IsLoading", viewModel.isLoading.value.toString())
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
            val rect = Rect()
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

