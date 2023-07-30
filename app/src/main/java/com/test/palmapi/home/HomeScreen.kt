package com.test.palmapi.home

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.test.palmapi.MainViewModel
import com.test.palmapi.bottombar.BottomBar
import com.test.palmapi.login.ProfileImage
import com.test.palmapi.navigation.Screens
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import com.test.palmapi.utils.CollapsedTopBarHomeScreen
import com.test.palmapi.utils.ExpandedTopBarHomeScreen
import com.test.palmapi.utils.NavigationDrawer
import com.test.palmapi.utils.getTimeAgo
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    viewModel: MainViewModel,
    photoUrl: String,
    name: String,
    email: String,
    type: String,
    uid: String,
) {
    val listState = rememberLazyListState()
    val isCollapsed: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val result by viewModel.getUniqueSaved().collectAsState(initial = emptyList())
    ModalNavigationDrawer(drawerContent = {
        NavigationDrawer(
            drawerState,
            photoUrl,
            name,
            email,
            viewModel = viewModel,
            navController = navHostController,
            type = type,
        )
    }, drawerState = drawerState, gesturesEnabled = drawerState.isOpen) {
        Scaffold(
            topBar = {
                CollapsedTopBarHomeScreen(
                    imageUrl = photoUrl,
                    isCollapsed = isCollapsed,
                    scroll = listState,
                    type = type,
                )
            },
            bottomBar = {
                BottomBar(navController = navHostController)
            }
        ) { padding ->
            println(padding)
            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(appGradient),
                    state = listState
                ) {
                    item {
                        ExpandedTopBarHomeScreen(
                            type = type,
                            imageUrl = photoUrl,
                            textValue = "",
                            onTextChange = {},
                            navController = navHostController,
                            openDrawerClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            },
                            closeDrawerClick = {
                                if (drawerState.isOpen) {
                                    coroutineScope.launch {
                                        drawerState.close()
                                    }
                                }
                            }
                        )
                    }
                    if (result.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = "Your Saved Chats",
                                    color = textColor,
                                    fontSize = 20.sp,
                                    fontFamily = monteSB,
                                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                                )
                            }
                        }
                        result.forEach {
                            Log.i("HomeScreen", "HomeScreen: $it")
                        }
                        items(result) { message ->
                            if (message.isUser) {
                                SavedCard(
                                    photoUrl = photoUrl,
                                    savedName = message.name,
                                    timeStamp = getTimeAgo(message.time),
                                    message = message.message ?: "",
                                    onClick = {
                                        if (message.name != "New Chat") {
                                            viewModel.savedName.value = message.name
                                            navHostController.navigate(Screens.SavedChat.route)
                                        } else {
                                            navHostController.navigate(Screens.NewChat.route)
                                        }
                                    }
                                )
                            }
                        }
                    } else {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    val currenanim2 by rememberLottieComposition(
                                        spec = LottieCompositionSpec.Asset("ai.json")
                                    )
                                    LottieAnimation(
                                        composition = currenanim2,
                                        iterations = 1,
                                        contentScale = ContentScale.Crop,
                                        speed = 0.75f,
                                        modifier = Modifier
                                            .size(200.dp)
                                    )

                                    Text(
                                        text = "AI is waiting for you to start a chat.",
                                        color = textColor,
                                        fontSize = 13.sp,
                                        fontFamily = monteSB,
                                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                                    )
                                    Spacer(modifier = Modifier.height(20.dp))
                                    val currenanim by rememberLottieComposition(
                                        spec = LottieCompositionSpec.Asset("empty.json")
                                    )
                                    LottieAnimation(
                                        composition = currenanim,
                                        iterations = 1,
                                        contentScale = ContentScale.Crop,
                                        speed = 0.5f,
                                        modifier = Modifier
                                            .size(200.dp)
                                    )

                                    Text(
                                        text = "Oops! You didn't even start a chat yet.",
                                        color = textColor,
                                        fontSize = 13.sp,
                                        fontFamily = monteSB,
                                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                                    )

                                    Spacer(modifier = Modifier.height(20.dp))
                                }
                            }
                        }
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp)
                        ) {
                            Spacer(modifier = Modifier.height(25.dp))
                            Text(
                                text = "Smartening Android",
                                color = textColor.copy(0.75f),
                                fontSize = 24.sp,
                                fontFamily = monteSB,
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Evolving You !!",
                                color = textColor.copy(0.75f),
                                fontSize = 19.sp,
                                fontFamily = monteSB,
                            )
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun SavedCard(
    photoUrl: String,
    savedName: String,
    timeStamp: String,
    message: String,
    onClick: () -> Unit = {}
) {

    Card(
        colors = CardDefaults.cardColors(CardColor),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickable {
                onClick()
            }
    ) {
        Box(modifier = Modifier) {
            Column {
                Text(
                    text = savedName,
                    modifier = Modifier.padding(10.dp),
                    color = textColor,
                    fontSize = 16.sp,
                    fontFamily = monteSB
                )
                Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(10.dp)) {
                    ProfileImage(
                        imageUrl = photoUrl,
                        modifier = Modifier
                            .size(30.dp)
                            .border(
                                width = 1.dp,
                                color = textColor.copy(0.5f),
                                shape = CircleShape
                            )
                            .padding(3.dp)
                            .clip(CircleShape),
                    )
                    Text(
                        text = message,
                        modifier = Modifier.padding(start = 10.dp, top = 5.dp),
                        color = textColor.copy(0.5f),
                        fontSize = 12.sp,
                        fontFamily = monteSB
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp, end = 10.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = timeStamp,
                modifier = Modifier.padding(0.dp),
                color = Color.Gray,
                fontSize = 10.sp,
                fontFamily = monteSB
            )
        }
    }
}


