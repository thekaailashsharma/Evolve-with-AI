package com.test.palmapi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val isDarkThemeEnabled: Boolean
    @Composable
    get() = isSystemInDarkTheme()


val lightThemeColors = listOf(
    Color(0xFFfcfdff),
    Color(0xFFf3f6fd),
    Color(0xFFe7eefa),
    Color(0xFFd9e5f9),
    Color(0xFFd9e5f9),
    Color(0xFFd9e5f9),
    Color(0xFFd6e2f8)
)

val darkThemeColors = listOf(
    Color(0xFF020509),
    Color(0xFF081226),
    Color(0xFF0d1b35),
    Color(0xFF102446),
    Color(0xFF152b52),
    Color(0xFF142a51),
)
val appBackground: Color
    @Composable
    get() = if (isDarkThemeEnabled) Color(0xFF102343) else Color(0xFFf9faff)

val appGradient : Brush
    @Composable
    get() = if (isDarkThemeEnabled) Brush.verticalGradient(
        colors = darkThemeColors,
        tileMode = TileMode.Clamp
    ) else Brush.verticalGradient( colors = lightThemeColors, tileMode = TileMode.Clamp)

val textColor: Color
    @Composable
    get() = if (isDarkThemeEnabled) Color.White else Color.Black

val CardColor :Color
    @Composable
    get() = if (isDarkThemeEnabled) Color(0xFF01122E) else Color(0xFFF3F2F2)