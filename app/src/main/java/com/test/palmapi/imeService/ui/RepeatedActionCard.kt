package com.test.palmapi.imeService.ui

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.test.palmapi.imeService.Action
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.textColor
import kotlinx.coroutines.launch

@Composable
fun RepeatedActionCard(
    action: Action? = null,
    onClick: () -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = action?.name ?: "",
                color = textColor,
                modifier = Modifier.clickable {
                    onClick()
                }
            )
        }
    }
}
