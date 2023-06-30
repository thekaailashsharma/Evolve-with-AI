package com.test.palmapi.imeService

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import com.test.palmapi.imeService.ui.KeyboardScreen

class ComposeKeyboardView(context: Context) : AbstractComposeView(context) {
    @Composable
    override fun Content() {
        KeyboardScreen()
    }
}
