package com.test.palmapi.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor

data class Services(
    val name: String,
    val desc: String,
    val anim: LottieCompositionSpec
)

@Composable
fun OurServices() {
    var items by remember { mutableStateOf(mutableListOf<Services>()) }
    items.add(
        Services(
            name = "Keyboard",
            desc = "Use our keyboard with inbuilt AI to get suggestions while typing!",
            anim = LottieCompositionSpec.Asset("keyboard.json")
        )
    )
    items.add(
        Services(
            name = "Accessibility",
            desc = "Use Accessibilty feature and access AI from anywhere!",
            anim = LottieCompositionSpec.Asset("accesibility.json")
        )
    )
    items.add(
        Services(
            name = "Text Recognition",
            desc = "Use our text recognition feature to recognize text from images !",
            anim = LottieCompositionSpec.Asset("textr.json")
        )
    )
    items.add(
        Services(
            name = "BarCode Scanner",
            desc = "Use our barcode scanner to scan barcodes from images !",
            anim = LottieCompositionSpec.Asset("qrcode.json")
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(appGradient)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Our Services",
                    color = textColor,
                    fontSize = 30.sp,
                    fontFamily = monteSB,
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                )
                Text(
                    text = "Check out our services below!",
                    color = textColor,
                    fontSize = 15.sp,
                    fontFamily = monteSB,
                    modifier = Modifier.padding(top = 0.dp, bottom = 20.dp)
                )

            }
        }
        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
            items(items.size) { index ->
                Spacer(modifier = Modifier.height(0.dp))
                ServicesCard(
                    spec = items[index].anim,
                    serviceName = items[index].name,
                    serviceDesc = items[index].desc
                )
            }

        }
    }
}

@Composable
fun ServicesCard(
    spec: LottieCompositionSpec,
    serviceName: String,
    serviceDesc: String,
) {

    Card(
        modifier = Modifier
            .size(400.dp, 300.dp)
            .padding(
                start = 7.dp,
                end = 7.dp,
                top = 7.dp,
                bottom = 7.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(appGradient)

        ) {
            Text(
                text = serviceName,
                color = textColor,
                fontSize = 12.sp,
                fontFamily = monteSB,
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )
            val currenanim by rememberLottieComposition(
                spec = spec
            )
            LottieAnimation(
                composition = currenanim,
                iterations = Int.MAX_VALUE,
                contentScale = ContentScale.Crop,
                speed = 0.5f,
                modifier = Modifier
                    .size(100.dp)
            )
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = serviceDesc,
                color = textColor,
                fontSize = 10.sp,
                fontFamily = monteSB,
                modifier = Modifier.padding(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = 10.dp,
                    end = 10.dp
                ),
                softWrap = true
            )

        }
    }
}