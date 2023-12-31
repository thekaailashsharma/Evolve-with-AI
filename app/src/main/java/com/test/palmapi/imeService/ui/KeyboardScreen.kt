package com.test.palmapi.imeService.ui

import android.content.Context
import android.util.Log
import android.view.KeyEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.test.palmapi.R
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.imeService.Action
import com.test.palmapi.imeService.IMEService
import com.test.palmapi.imeService.actions
import com.test.palmapi.imeService.callAPI
import com.test.palmapi.imeService.callAPIv2
import com.test.palmapi.imeService.translateActions
import com.test.palmapi.imeService.useActions
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.textColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.DisposableEffect


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
    val isAIAnimation = remember { mutableStateOf(false) }
    val isGeneratedAnimation = remember { mutableStateOf(false) }
    val isTranslateVisible = remember { mutableStateOf(false) }
    val generatedText = remember { mutableStateOf<String?>(null) }
    val currentAction = remember { mutableStateOf<Action?>(null) }
    val currentActionText = remember { mutableStateOf<String?>(null) }
    val clipboardText = remember { mutableStateOf<String?>(null) }
    val callAI = remember { mutableStateOf(false) }
    var text = remember { mutableStateOf("") }
    val ctx = LocalContext.current
    val clipboardManager = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData: ClipData? = clipboardManager.primaryClip
    val datastore = UserDatastore(ctx)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = generatedText.value) {
        if (generatedText.value != null) {
            delay(500)
            isGeneratedAnimation.value = false
        }
    }
    DisposableEffect(ctx) {
        val clipboardManager = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val listener = ClipboardManager.OnPrimaryClipChangedListener {
            clipboardText.value = getClipboardText(ctx) ?: ""
        }

        clipboardManager.addPrimaryClipChangedListener(listener)

        onDispose {
            clipboardManager.removePrimaryClipChangedListener(listener)
        }
    }

    Column(
        modifier = Modifier
            .background(appGradient)
            .fillMaxWidth()
    ) {
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
                        callAI.value = !callAI.value
                        currentAction.value = null
                        currentActionText.value = null
                        isGeneratedAnimation.value = false
                        if (generatedText.value != null) {
                            generatedText.value = null
                        }
                    }
            )
            if (!callAI.value && clipboardText.value != null) {
                Row(
                    modifier = Modifier.fillMaxWidth().offset(x = (-10).dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${clipboardText.value?.take(10)}...",
                        maxLines = 1,
                        color = textColor,
                        modifier = Modifier.clickable {
                            text.value = clipboardText.value ?: ""
                            (ctx as IMEService).currentInputConnection.commitText(
                                "\n${text.value}",
                                text.value.length
                            )
                        }
                    )
                }
            }

            AnimatedVisibility(
                visible = callAI.value,
                enter = slideInHorizontally(tween(300), initialOffsetX = {
                    it
                }) + fadeIn(),
                exit = slideOutHorizontally(tween(300), targetOffsetX = {
                    it
                }) + fadeOut()
            ) {
                LazyRow(contentPadding = PaddingValues(7.dp)) {
                    if (generatedText.value != null) {
                        items(useActions) { useAction ->
                            RepeatedActionCard(action = useAction) {
                                if (useAction.name == "Use text") {
                                    val inputConnection =
                                        (ctx as IMEService).currentInputConnection
                                    inputConnection.performContextMenuAction(android.R.id.selectAll)
                                    inputConnection.commitText("", 1)
                                    (ctx as IMEService).currentInputConnection.commitText(
                                        "\n${generatedText.value}",
                                        generatedText.value?.length ?: 0
                                    )
                                } else if (useAction.name == "Regenerate") {
                                    coroutineScope.launch {
                                        isGeneratedAnimation.value = true
                                        currentAction.value = currentAction.value
                                        currentActionText.value = text.value
                                        generatedText.value =
                                            currentAction.value?.let {
                                                callAPIv2(
                                                    action = it,
                                                    text = currentActionText.value ?: ""
                                                )
                                            }
                                    }
                                }
                            }
                        }
                    } else {
                        if (!isTranslateVisible.value) {
                            items(actions) { action ->
                                RepeatedActionCard(action = action) {
                                    if (action.name != "Translate (Beta)") {
                                        coroutineScope.launch {
                                            isGeneratedAnimation.value = true
                                            currentAction.value = action
                                            generatedText.value =
                                                callAPIv2(
                                                    action = action,
                                                    text = text.value
                                                )
                                        }
                                    } else {
                                        isTranslateVisible.value = true
                                    }
                                }
                            }
                        } else {
                            items(translateActions) { action ->
                                RepeatedActionCard(action = action) {
                                    coroutineScope.launch {
                                        isGeneratedAnimation.value = true
                                        currentAction.value = action
                                        generatedText.value =
                                            callAPIv2(
                                                action = action,
                                                text = currentActionText.value ?: ""
                                            )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Divider(color = textColor, thickness = 1.dp)
        AnimatedVisibility(
            visible = isGeneratedAnimation.value,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300))
        ) {
            FixedHeightBox(modifier = Modifier.fillMaxWidth(), height = 232.dp) {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val currenanim2 by rememberLottieComposition(
                        spec = LottieCompositionSpec.Asset("ai.json")
                    )
                    LottieAnimation(
                        composition = currenanim2,
                        iterations = Int.MAX_VALUE,
                        contentScale = ContentScale.Crop,
                        speed = 0.75f,
                        modifier = Modifier
                            .size(150.dp)
                    )
                }
            }
        }
        if (!isGeneratedAnimation.value) {
            if (checkAICall(text.value)) {
                Log.i("KeyboardAPIBefore", text.value)
                val extractedText = extractTextInsideParentheses(text.value)
                Log.i("KeyboardAPIAfter", extractedText ?: "null")
                LaunchedEffect(Unit) {
                    val response = callAPI(extractedText ?: "")
                    Log.i("KeyboardAPI", response.toString())
                    if (response != null) {
                        (ctx as IMEService).currentInputConnection.commitText(
                            "\n$response",
                            response.length
                        )
                        datastore.storeText(extractedText ?: "")

                    } else {
                        (ctx as IMEService).currentInputConnection.commitText(
                            "\nSomething went wrong!",
                            20
                        )
                    }
                    text.value = ""
                    isAIAnimation.value = true
                }
            }
            if (generatedText.value == null) {
                if (!numericKeyBoard.value) {
                    lowerKeyMatrix.forEach { row ->
                        FixedHeightBox(modifier = Modifier.fillMaxWidth(), height = 58.dp) {
                            Row(Modifier) {
                                row.forEach { key ->
                                    if (key.icon != null) {
                                        IconKey(
                                            ctx,
                                            keyboardKey = key.icon,
                                            capsLockState = capsLockState,
                                            isAICalled = isAIAnimation,
                                            text = text,
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else {
                                        KeyboardKey(
                                            ctx,
                                            keyboardKey = (if (capsLockState.value)
                                                key.key?.uppercase() else key.key).toString(),
                                            capsLockState = capsLockState,
                                            numericKeyBoardState = numericKeyBoard,
                                            isAICalled = isAIAnimation,
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
                                            ctx,
                                            keyboardKey = key.icon,
                                            capsLockState = capsLockState,
                                            isAICalled = isAIAnimation,
                                            text = text,
                                            modifier = Modifier.weight(1f)
                                        )
                                    } else {
                                        KeyboardKey(
                                            ctx,
                                            keyboardKey = (if (capsLockState.value)
                                                key.key?.uppercase() else key.key).toString(),
                                            capsLockState = capsLockState,
                                            numericKeyBoardState = numericKeyBoard,
                                            isAICalled = isAIAnimation,
                                            text = text,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 15.dp)
                        .fillMaxWidth()
                        .height(232.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = generatedText.value ?: "", color = textColor)
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
    ctx: Context,
    keyboardKey: Int,
    capsLockState: MutableState<Boolean>,
    isAICalled: MutableState<Boolean>,
    text: MutableState<String>,
    modifier: Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed = interactionSource.collectIsPressedAsState()
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

                        text.value = text.value.dropLast(1)
                        (ctx as IMEService).currentInputConnection.sendKeyEvent(
                            KeyEvent(
                                KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_DEL,
                            )
                        )

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
    ctx: Context,
    keyboardKey: String,
    capsLockState: MutableState<Boolean>,
    numericKeyBoardState: MutableState<Boolean>,
    isAICalled: MutableState<Boolean>,
    text: MutableState<String>,
    modifier: Modifier
) {
    val datastore = UserDatastore(ctx)
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    val pressed = interactionSource.collectIsPressedAsState()
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
                        text.value += if (keyboardKey.lowercase() == "space") " " else keyboardKey
                        (ctx as IMEService).currentInputConnection.commitText(
                            if (keyboardKey.lowercase() == "space") " "
                            else keyboardKey,
                            keyboardKey.length,
                        )
                        coroutineScope.launch {
                            datastore.storeText(text.value)
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


fun checkAICall(inputText: String): Boolean {
    val pattern = Pattern.compile("\\(\\((.*?)\\)\\)", Pattern.DOTALL)
    val matcher = pattern.matcher(inputText)
    val result = mutableListOf<String>()

    if (matcher.find()) {
        return true
    }

    return false
}

fun extractTextInsideParentheses(inputText: String): String? {
    val pattern = Pattern.compile("\\(\\((.*?)\\)\\)", Pattern.DOTALL)
    val matcher = pattern.matcher(inputText)
    var result: String? = ""

    while (matcher.find()) {
        result = matcher.group(1)
    }

    return result
}

fun getClipboardText(context: Context): String? {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData: ClipData? = clipboardManager.primaryClip

    if (clipData != null && clipData.itemCount > 0) {
        val item = clipData.getItemAt(0)
        return item.text?.toString()
    }

    return null
}

