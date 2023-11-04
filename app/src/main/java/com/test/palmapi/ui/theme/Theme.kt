package com.test.palmapi.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.ui.theme.colorPalette.BlackColors
import com.test.palmapi.ui.theme.colorPalette.CelestialColors
import com.test.palmapi.ui.theme.colorPalette.DarkColors
import com.test.palmapi.ui.theme.colorPalette.LightColors

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun PalmApiTheme(
    darkTheme: Boolean = true,
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val datastore = UserDatastore(context = context)
    val currentTheme by datastore.getTheme.collectAsState(initial = ThemeMode.Celestial.name)
    val themeMode = when (currentTheme) {
        ThemeMode.Celestial.name -> ThemeMode.Celestial
        ThemeMode.Black.name -> ThemeMode.Black
        ThemeMode.Dark.name -> ThemeMode.Dark
        ThemeMode.Light.name -> ThemeMode.Light
        else -> ThemeMode.Light
    }
    val colorScheme = when (themeMode) {
        ThemeMode.Celestial -> CelestialColors
        ThemeMode.Light -> LightColors
        ThemeMode.Dark -> DarkColors
        ThemeMode.Black -> BlackColors
    }


    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
