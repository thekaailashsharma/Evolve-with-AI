package com.test.palmapi.savedChat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.test.palmapi.MainViewModel
import com.test.palmapi.R
import com.test.palmapi.database.chats.ChatMessage
import com.test.palmapi.newChat.ChatCard
import com.test.palmapi.newChat.RepeatedCard
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.monteNormal
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import com.test.palmapi.utils.BottomSheetContent
import com.test.palmapi.utils.NewChatBottomBar
import com.test.palmapi.utils.NewChatTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedChat(
    viewModel: MainViewModel,
    navController: NavHostController,
    photoUrl: String
) {

    var text by remember {
        mutableStateOf(TextFieldValue(""))
    }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val result by viewModel.savedMessages.collectAsState(initial = listOf())
    LaunchedEffect(key1 = result) {
        try {
            if (result.isNotEmpty()) {
                lazyListState.animateScrollToItem(result.size - 1)
            }
        } catch (e: Exception) {
            Log.i("Message", e.toString())
        }
    }
    Log.i("ProfileImage", photoUrl)
    val modalSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            initialValue = SheetValue.Hidden,
            skipPartiallyExpanded = false
        )
    )

    BottomSheetScaffold(
        sheetContent = {
            BottomSheetContent(viewModel)
        },
        sheetContainerColor = CardColor.copy(0.95f),
        scaffoldState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        sheetPeekHeight = 0.dp,
    ) {
        Scaffold(
            topBar = {
                NewChatTopBar(
                    navHostController = navController,
                    onSave = {
                        coroutineScope.launch {
                            modalSheetState.bottomSheetState.expand()
                        }
                    }
                )
            },
            bottomBar = {
                NewChatBottomBar(
                    viewModel = viewModel,
                    text = text,
                    onTextChange = {
                        text = it
                    },
                    onClick = {
                        viewModel.insertChat(
                            ChatMessage(
                                message = text.text,
                                time = System.currentTimeMillis(),
                                isUser = true
                            )
                        )
                        viewModel.message.value = text.text
                        viewModel.getApiData()
                        coroutineScope.launch {
                            try {
                                if (result.isNotEmpty()) {
                                    lazyListState.animateScrollToItem(result.size - 1)
                                }
                            } catch (e: Exception) {
                                Log.i("Message", e.toString())
                            }
                        }
                        text = TextFieldValue("")

                    }
                )
            }
        ) {
            println(it)
            if (result.isEmpty()) {
                Column(
                    modifier = Modifier
                        .background(appGradient)
                        .fillMaxSize()
                        .padding(top = 150.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontFamily = monteNormal,
                                color = textColor,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Black
                            )
                        ) {
                            append("Hello, Ask Me")
                        }
                        append("\n")
                        withStyle(
                            SpanStyle(
                                fontFamily = monteNormal,
                                color = textColor,
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("Anything ...")
                        }
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "I'm your assistant.",
                        color = textColor,
                        fontFamily = monteSB
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    RepeatedCard(
                        icon = R.drawable.beta,
                        description = "I'm still under development"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    RepeatedCard(
                        icon = R.drawable.remember,
                        description = "I can remember our conversations"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    RepeatedCard(
                        icon = R.drawable.privacy,
                        description = "Your Privacy is completely secured"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    RepeatedCard(
                        icon = R.drawable.tip,
                        description = "Long Press on Chat to see options"
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreHoriz,
                            contentDescription = "more",
                            tint = textColor.copy(0.45f),
                            modifier = Modifier.size(40.dp)
                        )

                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .background(appGradient)
                        .fillMaxSize()
                        .padding(top = 30.dp),
                    contentPadding = PaddingValues(top = 40.dp, bottom = 130.dp),
                    state = lazyListState,
                ) {
                    items(result) { message ->
                        ChatCard(
                            text = message.message ?: "",
                            isUser = message.isUser,
                            viewModel = viewModel,
                            imageUrl = photoUrl,
                            time = message.time,
                        )
                    }

                }
            }
        }
    }
}
