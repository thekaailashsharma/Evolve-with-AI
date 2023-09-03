package com.test.palmapi.theming

import android.content.Intent
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.test.palmapi.BaseActivity
import com.test.palmapi.R
import com.test.palmapi.bottombar.BottomBar
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.ui.theme.ThemeMode
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import kotlinx.coroutines.launch


data class Themes(
    val name: String,
    val desc: String,
    val image: Int,
    val themeMode: ThemeMode
)

val themeRow1 = listOf(
    Themes(
        name = "Celestial",
        desc = "Serene blues and whites.",
        image = R.drawable.theme_celestial,
        themeMode = ThemeMode.Celestial
    ),
    Themes(
        name = "Aqua Bliss",
        desc = "Refreshing blues, vibrant aquas.",
        image = R.drawable.theme_aquabliss,
        themeMode = ThemeMode.AquaBliss
    ),
)
val themeRow2 = listOf(
    Themes(
        name = "Retrofuturist",
        desc = "Vibrant purples, neon accents.",
        image = R.drawable.theme_retrofuturist,
        themeMode = ThemeMode.Retrofuturist
    ),
    Themes(
        name = "Cornsilk",
        desc = "Soothing, blend of colors.",
        image = R.drawable.theme_cornsilk,
        themeMode = ThemeMode.CornSilk
    ),
)

@Composable
fun MultipleThemes(navController: NavHostController) {
    val isAnimationVisible = remember { mutableStateOf(false) }
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(appGradient)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .background(appGradient)
                    .then(if (isAnimationVisible.value) Modifier.blur(10.dp) else Modifier),
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Choose Theme",
                                color = textColor,
                                fontSize = 30.sp,
                                fontFamily = monteSB,
                                modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
                            )
                            Text(
                                text = "We offer wide range of aesthetic themes",
                                color = textColor,
                                fontSize = 15.sp,
                                fontFamily = monteSB,
                                modifier = Modifier.padding(top = 0.dp, bottom = 20.dp)
                            )

                        }
                    }
                }
                item {
                    TwoThemesCard(
                        image1 = themeRow1[0].image,
                        image2 = themeRow1[1].image,
                        themeDesc1 = themeRow1[0].desc,
                        themeDesc2 = themeRow1[1].desc,
                        themeName1 = themeRow1[0].name,
                        themeName2 = themeRow1[1].name,
                        themeMode1 = themeRow1[0].themeMode,
                        themeMode2 = themeRow1[1].themeMode,
                        isAnimationVisible = isAnimationVisible
                    )
                }
                item {
                    TwoThemesCard(
                        image1 = themeRow2[0].image,
                        image2 = themeRow2[1].image,
                        themeDesc1 = themeRow2[0].desc,
                        themeDesc2 = themeRow2[1].desc,
                        themeName1 = themeRow2[0].name,
                        themeName2 = themeRow2[1].name,
                        themeMode1 = themeRow2[0].themeMode,
                        themeMode2 = themeRow2[1].themeMode,
                        isAnimationVisible = isAnimationVisible
                    )
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                    ) {
                        Spacer(modifier = Modifier.height(25.dp))
                        Text(
                            text = "Evolve adjusts",
                            color = MaterialTheme.colorScheme.onError.copy(0.75f),
                            fontSize = 26.sp,
                            fontFamily = monteSB,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "according to your Mood",
                            color = MaterialTheme.colorScheme.onError.copy(0.5f),
                            fontSize = 19.sp,
                            fontFamily = monteSB,
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
            if (isAnimationVisible.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    val currenanim2 by rememberLottieComposition(
                        spec = LottieCompositionSpec.Asset("themes.json")
                    )
                    LottieAnimation(
                        composition = currenanim2,
                        iterations = Int.MAX_VALUE,
                        contentScale = ContentScale.Crop,
                        speed = 2.2f,
                        modifier = Modifier
                            .size(200.dp)
                    )
                }
            }
        }

    }
}

@Composable
fun TwoThemesCard(
    image1: Int,
    themeName1: String,
    themeDesc1: String,
    themeMode1: ThemeMode,
    image2: Int,
    themeName2: String,
    themeDesc2: String,
    themeMode2: ThemeMode,
    isAnimationVisible: MutableState<Boolean>
) {
    Row(
        modifier = Modifier.fillMaxWidth(1f),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.5f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ChooseThemesCard(
                image = image1,
                themeName = themeName1,
                themeDesc = themeDesc1,
                themeMode = themeMode1,
                isAnimationVisible = isAnimationVisible
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ChooseThemesCard(
                image = image2,
                themeName = themeName2,
                themeDesc = themeDesc2,
                themeMode = themeMode2,
                isAnimationVisible = isAnimationVisible
            )
        }
    }

}


@Composable
fun ChooseThemesCard(
    image: Int,
    themeName: String,
    themeDesc: String,
    themeMode: ThemeMode,
    isAnimationVisible: MutableState<Boolean>
) {
    val context = LocalContext.current
    val dataStore = UserDatastore(context)
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = isAnimationVisible.value) {
        if (isAnimationVisible.value) {
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                val refresh = Intent(context, BaseActivity::class.java)
                refresh.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                ContextCompat.startActivity(context, refresh, null)
            }, 2000)
        }
    }

    Card(
        modifier = Modifier
            .wrapContentHeight()
            .padding(
                start = 7.dp,
                end = 7.dp,
                top = 7.dp,
                bottom = 7.dp
            )
            .clickable {
                isAnimationVisible.value = true
                coroutineScope.launch {
                    dataStore.saveTheme(themeMode.name)
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(appGradient)

        ) {
            Text(
                text = themeName,
                color = textColor,
                fontSize = 12.sp,
                fontFamily = monteSB,
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp),
                fontWeight = FontWeight.Black
            )

            Image(
                painter = painterResource(id = image),
                contentDescription = themeName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(7.dp))
                    .shadow(10.dp)
            )
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = themeDesc,
                color = textColor,
                fontSize = 9.sp,
                fontFamily = monteSB,
                modifier = Modifier.padding(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = 10.dp,
                    end = 10.dp
                ),
                softWrap = true
            )
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                onClick = {

                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Apply",
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
}


