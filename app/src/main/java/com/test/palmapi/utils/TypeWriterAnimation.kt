package com.test.palmapi.utils

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import kotlinx.coroutines.delay
import kotlin.streams.toList

@Composable
fun TypewriterText(
    modifier: Modifier = Modifier,
    texts: List<String>,
    text: String,
    delay: Long = 160,
    fontFamily: FontFamily = monteSB,
    fontSize: TextUnit = 20.sp,
    softWrap: Boolean = false,
    afterAnimation: () -> Unit = {},
) {
    var textIndex by remember {
        mutableStateOf(0)
    }
    var textToDisplay by remember {
        mutableStateOf("")
    }

    LaunchedEffect(
        key1 = texts,
    ) {
        try {
            while (textIndex < texts.size) {
                if (textIndex == texts.size - 1) {
                    delay(1000)
                    afterAnimation()
                }
                delay(1000)
                texts[textIndex].forEachIndexed { charIndex, _ ->
                    textToDisplay = texts[textIndex]
                        .substring(
                            startIndex = 0,
                            endIndex = charIndex + 1,
                        )
                    delay(delay)
                }
                textIndex = (textIndex + 1)
                delay(6000)
            }
        } catch (e: Exception) {
            Log.i("AfterAnimation", "AfterAnimationCalled")
            afterAnimation()
            Log.i("AfterAnimation", "AfterAnimationCallOver")
            textToDisplay = text
        }
    }

    Text(
        text = textToDisplay,
        fontSize = fontSize,
        modifier = modifier,
        fontFamily = fontFamily,
        softWrap = softWrap,
        color = textColor
    )
}


fun String.splitToCodePoints(): List<String> {
    return codePoints()
        .toList()
        .map {
            String(Character.toChars(it))
        }
}