package com.test.palmapi.imeService.ui

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.palmapi.R
import com.test.palmapi.imeService.IMEService
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.isDarkThemEnabled
import com.test.palmapi.ui.theme.textColor

data class KeyMatrix(
    val key: String? = null,
    val icon: Int? = null,
    val onClick: () -> Unit = {}
)

@Composable
fun KeyboardScreen() {
    val lowerKeyMatrix = listOf(
        listOf(
            KeyMatrix("q"),
            KeyMatrix("w"),
            KeyMatrix("e"),
            KeyMatrix("r"),
            KeyMatrix("t"),
            KeyMatrix("y"),
            KeyMatrix("u"),
            KeyMatrix("i"),
            KeyMatrix("o"),
            KeyMatrix("p"),
        ),
        listOf(
            KeyMatrix("a"),
            KeyMatrix("s"),
            KeyMatrix("d"),
            KeyMatrix("f"),
            KeyMatrix("g"),
            KeyMatrix("h"),
            KeyMatrix("j"),
            KeyMatrix("k"),
            KeyMatrix("l"),
        ),
        listOf(
            KeyMatrix(icon = R.drawable.capslock),
            KeyMatrix("z"),
            KeyMatrix("x"),
            KeyMatrix("c"),
            KeyMatrix("v"),
            KeyMatrix("b"),
            KeyMatrix("n"),
            KeyMatrix("m"),
            KeyMatrix(icon = R.drawable.backspace),
        ),
        listOf(
            KeyMatrix("123"),
            KeyMatrix("/"),
            KeyMatrix("@"),
            KeyMatrix("space"),
            KeyMatrix("."),
            KeyMatrix(icon = R.drawable.enter)
        ),
    )
    val numericKeyMatrix = listOf(
        listOf(
            KeyMatrix("1"),
            KeyMatrix("2"),
            KeyMatrix("3"),
            KeyMatrix("4"),
            KeyMatrix("5"),
            KeyMatrix("6"),
            KeyMatrix("7"),
            KeyMatrix("8"),
            KeyMatrix("9"),
            KeyMatrix("0"),
        ),
        listOf(
            KeyMatrix("@"),
            KeyMatrix("#"),
            KeyMatrix("$"),
            KeyMatrix("_"),
            KeyMatrix("&"),
            KeyMatrix("-"),
            KeyMatrix("+"),
            KeyMatrix("("),
            KeyMatrix(")"),
            KeyMatrix("/"),
        ),
        listOf(
            KeyMatrix("<"),
            KeyMatrix(">"),
            KeyMatrix("*"),
            KeyMatrix("''"),
            KeyMatrix(":"),
            KeyMatrix(";"),
            KeyMatrix("!"),
            KeyMatrix("?"),
            KeyMatrix("&"),
            KeyMatrix(icon = R.drawable.backspace),
        ),

        listOf(
            KeyMatrix("ABC"),
            KeyMatrix(","),
            KeyMatrix("."),
            KeyMatrix("space"),
            KeyMatrix("*"),
            KeyMatrix(icon = R.drawable.enter)
        ),
    )
    val capsLockState = remember { mutableStateOf(false) }
    val numericKeyBoard = remember { mutableStateOf(false) }
    val isAICalled = remember { mutableStateOf(false) }
    var text = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .background(appGradient)
            .fillMaxWidth()
    ) {
        if (isAICalled.value) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val containerColor = if (isDarkThemEnabled) Color(0xFF162130)
                else Color(0xFFe6e9f1)
                OutlinedTextField(
                    value = text.value,
                    onValueChange = {
                        text.value = it
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
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painterResource(id = R.drawable.appicon),
                contentDescription = "",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(30.dp)
                    .clickable {
                        isAICalled.value = !isAICalled.value
                    }
            )
            Icon(
                painterResource(id = R.drawable.mic),
                contentDescription = "",
                tint = textColor,
                modifier = Modifier.size(25.dp)
            )
        }
        Divider(color = textColor, thickness = 1.dp)
        if (!numericKeyBoard.value) {
            lowerKeyMatrix.forEach { row ->
                FixedHeightBox(modifier = Modifier.fillMaxWidth(), height = 58.dp) {
                    Row(Modifier) {
                        row.forEach { key ->
                            if (key.icon != null) {
                                IconKey(
                                    keyboardKey = key.icon,
                                    capsLockState = capsLockState,
                                    isAICalled = isAICalled,
                                    text = text,
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                KeyboardKey(
                                    keyboardKey = (if (capsLockState.value)
                                        key.key?.uppercase() else key.key).toString(),
                                    capsLockState = capsLockState,
                                    numericKeyBoardState = numericKeyBoard,
                                    isAICalled = isAICalled,
                                    text = text,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        } else {
            numericKeyMatrix.forEach { row ->
                FixedHeightBox(modifier = Modifier.fillMaxWidth(), height = 56.dp) {
                    Row(Modifier) {
                        row.forEach { key ->
                            if (key.icon != null) {
                                IconKey(
                                    keyboardKey = key.icon,
                                    capsLockState = capsLockState,
                                    isAICalled = isAICalled,
                                    text = text,
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                KeyboardKey(
                                    keyboardKey = (if (capsLockState.value)
                                        key.key?.uppercase() else key.key).toString(),
                                    capsLockState = capsLockState,
                                    numericKeyBoardState = numericKeyBoard,
                                    isAICalled = isAICalled,
                                    text = text,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FixedHeightBox(modifier: Modifier, height: Dp, content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        val h = height.roundToPx()
        layout(constraints.maxWidth, h) {
            placeables.forEach { placeable ->
                placeable.place(x = 0, y = kotlin.math.min(0, h - placeable.height))
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun IconKey(
    keyboardKey: Int,
    capsLockState: MutableState<Boolean>,
    isAICalled: MutableState<Boolean>,
    text: MutableState<String>,
    modifier: Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed = interactionSource.collectIsPressedAsState()
    val ctx = LocalContext.current
    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Icon(
            painterResource(id = keyboardKey),
            contentDescription = "",
            tint = if (keyboardKey == R.drawable.enter)
                Color.Unspecified
            else if (keyboardKey == R.drawable.capslock && capsLockState.value) Color.Green
            else textColor,
            modifier = Modifier
                .size(if (keyboardKey == R.drawable.enter) 55.dp else 38.dp)
                .padding(
                    start = 7.dp,
                    end = 10.dp,
                    top = 7.dp,
                    bottom = if (keyboardKey == R.drawable.enter) 5.dp else 15.dp

                )
                .then(if (keyboardKey == R.drawable.capslock)
                    Modifier.clickable {
                        capsLockState.value = !capsLockState.value
                    } else Modifier
                )
                .then(if (keyboardKey == R.drawable.backspace)
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        if (isAICalled.value) {
                            text.value = text.value.dropLast(1)
                        } else {
                            (ctx as IMEService).currentInputConnection.sendKeyEvent(
                                KeyEvent(
                                    KeyEvent.ACTION_DOWN,
                                    KeyEvent.KEYCODE_DEL,
                                )
                            )
                        }
                    } else Modifier
                )
                .then(if (keyboardKey == R.drawable.enter)
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        (ctx as IMEService).currentInputConnection.sendKeyEvent(
                            KeyEvent(
                                KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_ENTER,
                            )
                        )
                    } else Modifier
                )

        )


//        Icon(
//            painterResource(id = keyboardKey),
//            "",
//            Modifier
//                .fillMaxWidth()
//                .size(24.dp)
//                .padding(2.dp)
//                .padding(
//                    start = 12.dp,
//                    end = 12.dp,
//                    top = 16.dp,
//                    bottom = 16.dp
//                ),
//            tint = textColor
//
//        )
//        if (pressed.value) {
//            Text(
//                keyboardKey,
//                Modifier
//                    .fillMaxWidth()
//                    .border(1.dp, Color.Black)
//                    .background(textColor)
//                    .padding(
//                        start = 16.dp,
//                        end = 16.dp,
//                        top = 16.dp,
//                        bottom = 48.dp
//                    )
//            )
//        }
    }
}

@Composable
fun KeyboardKey(
    keyboardKey: String,
    capsLockState: MutableState<Boolean>,
    numericKeyBoardState: MutableState<Boolean>,
    isAICalled: MutableState<Boolean>,
    text: MutableState<String>,
    modifier: Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }
    val pressed = interactionSource.collectIsPressedAsState()
    val ctx = LocalContext.current
    Box(modifier = modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
        Text(
            if (keyboardKey.lowercase() == "space") "space" else keyboardKey,
            fontSize = if (keyboardKey.lowercase() == "space") 13.sp else 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .then(if (keyboardKey != "123" && keyboardKey != "ABC")
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        if (isAICalled.value) {
                            text.value += keyboardKey
                        } else {
                            (ctx as IMEService).currentInputConnection.commitText(
                                if (keyboardKey.lowercase() == "space") " "
                                else keyboardKey,
                                keyboardKey.length,
                            )
                        }
                    }
                else
                    Modifier.clickable {
                        numericKeyBoardState.value = !numericKeyBoardState.value
                    }
                )
                .padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 16.dp,
                    bottom = 16.dp
                ),
            color = textColor

        )
        if (pressed.value) {
            Text(
                if (capsLockState.value) keyboardKey.uppercase() else keyboardKey.lowercase(),
                Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardColor)
                    .background(textColor)
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 48.dp
                    )
            )
        }
    }
}
