package com.test.palmapi.ui.theme

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.dp

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val COLLAPSED_TOP_BAR_HEIGHT = 80.dp
val EXPANDED_TOP_BAR_HEIGHT = 360.dp

val googleColors = listOf(
    Color(0xFF4285F4),
    Color(0xFFDB4437),
    Color(0xFFF4B400),
    Color(0xFF4285F4),
    Color(0xFF0F9D58),
    Color(0xFFDB4437),
)

val twitterColors = listOf(
    Color(0xFF1DA1F2),
    Color(0xFF1DA1F2),
    Color(0xFF1DA1F2),
)

val githubColors = listOf(
    Color(0xFFfafafa),
    Color(0xFF333333),
    Color(0xFFf5f5f5),
    Color(0xFF333333),
)

val isDarkThemEnabled: Boolean
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
val appGradient: Brush
    @Composable
    get() = if (isDarkThemEnabled) Brush.verticalGradient(
        colors = darkThemeColors,
        tileMode = TileMode.Clamp
    ) else Brush.verticalGradient(colors = lightThemeColors, tileMode = TileMode.Clamp)

val textColor: Color
    @Composable
    get() = if (isDarkThemEnabled) Color.White else Color.Black

val buttonColor: Color
    @Composable
    get() = if (isDarkThemEnabled) Color(0xFF142a51) else Color(0xFFd6e2f8)

val CardColor: Color
    @Composable
    get() = if (isDarkThemEnabled) Color(0xFF01122E) else Color(0xFFF3F2F2)

fun openDeviceThemeSettings(context: Context) {
    val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
    context.startActivity(intent)
}