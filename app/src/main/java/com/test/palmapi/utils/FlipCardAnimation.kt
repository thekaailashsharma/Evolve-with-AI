package com.test.palmapi.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.test.palmapi.services.CardState

class TransitionData(
    rotation: State<Float>,
    animateFront: State<Float>,
    animateBack: State<Float>
) {
    val rotation by rotation
    val animateFront by animateFront
    val animateBack by animateBack
}

@Composable
fun updateTransitionData(cardState: CardState): TransitionData {
    val transition = updateTransition(cardState, label = "")
    val rotation = transition.animateFloat(
        transitionSpec = {
            tween(500)
        },
        label = ""
    ) { state ->
        when (state) {
            CardState.Front -> 0f
            CardState.Back -> -180f
        }
    }

    val animateFront = transition.animateFloat(
        transitionSpec = {
            tween(500)
        },
        label = ""
    ) { state ->
        when (state) {
            CardState.Front -> 1f
            CardState.Back -> 0f
        }
    }
    val animateBack = transition.animateFloat(
        transitionSpec = {
            tween(500)
        },
        label = ""
    ) { state ->
        when (state) {
            CardState.Front -> 0f
            CardState.Back -> 1f
        }
    }

    return remember(transition) { TransitionData(rotation, animateFront, animateBack) }
}