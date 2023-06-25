package com.test.palmapi.newChat

import android.util.Log
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.test.palmapi.MainViewModel
import com.test.palmapi.R
import com.test.palmapi.database.ChatMessage
import com.test.palmapi.login.ProfileImage
import com.test.palmapi.ui.theme.CardColor
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.ui.theme.monteBold
import com.test.palmapi.ui.theme.monteNormal
import com.test.palmapi.ui.theme.monteSB
import com.test.palmapi.ui.theme.textColor
import kotlinx.coroutines.launch

@Composable
fun NewChat(
    navHostController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val user by remember { mutableStateOf(Firebase.auth.currentUser) }
    var text by remember {
        mutableStateOf(TextFieldValue(""))
    }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val result by viewModel.allMessages.collectAsState(initial = listOf())
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val density = LocalDensity.current
    LaunchedEffect(key1 = result) {
        lazyListState.animateScrollToItem(result.size - 1)
    }
    Log.i("ProfileImage", user?.photoUrl.toString())
    Scaffold(
        topBar = { NewChatTopBar() },
        bottomBar = {
            NewChatBottomBar(
                viewModel = viewModel,
                text = text,
                onTextChange = {
                    text = it
                },
                onClick = {
                    viewModel.saveChat(
                        ChatMessage(
                            message = text.text,
                            time = System.currentTimeMillis(),
                            isUser = true
                        )
                    )
                    viewModel.message.value = text.text
                    viewModel.getApiData()
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(result.size - 1)
                    }
                    text = TextFieldValue("")

                }
            )
        }
    ) {

        Log.i("Message8", result.toString())

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
                    .padding(top = 30.dp)
                    .clickable {
                        isContextMenuVisible = false
                    },
                contentPadding = PaddingValues(top = 40.dp, bottom = 130.dp),
                state = lazyListState,
            ) {
                items(result) { message ->
                    ChatCard(
                        text = message.message ?: "",
                        isUser = message.isUser,
                        viewModel = viewModel,
                        imageUrl = user?.photoUrl.toString(),
                    )
                }

            }
        }
    }
}

@Composable
fun ChatCard(
    text: String,
    isUser: Boolean,
    imageUrl: String,
    viewModel: MainViewModel
) {
    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var pressOffset by remember {
        mutableStateOf(DpOffset.Zero)
    }
    var itemHeight by remember {
        mutableStateOf(0.dp)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val density = LocalDensity.current
    Row(
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .then(
                if (isUser) Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        end = 20.dp,
                        top = 7.dp,
                        bottom = 7.dp
                    ) else Modifier.padding(
                    start = 10.dp,
                    end = 20.dp,
                    top = 7.dp,
                    bottom = 7.dp
                )
            )
            .clickable {
                isContextMenuVisible = false
                viewModel.isBlurred.value = false
            }
            .onSizeChanged {
                itemHeight = with(density) { it.height.toDp() }
            }
            .indication(interactionSource, LocalIndication.current)
            .pointerInput(true) {
                detectTapGestures(
                    onLongPress = {
                        isContextMenuVisible = true
                        viewModel.isBlurred.value = true
                        pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                    },
                    onPress = {
                        isContextMenuVisible = false
                        viewModel.isBlurred.value = false
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    }
                )
            }
            .then(
                if (viewModel.isBlurred.value && !isContextMenuVisible)
                    Modifier.blur(10.dp, 10.dp, BlurredEdgeTreatment.Unbounded) else Modifier
            )
    ) {
        if (!isUser) {
            ProfileImage(
                imageUrl = R.drawable.appicon,
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
        }
        Card(
            modifier = Modifier
                .then(if (isUser) Modifier
                    .fillMaxWidth(0.86f)
                    .padding(
                        start = 7.dp,
                        end = 7.dp,
                        top = 0.dp,
                        bottom = 7.dp
                    ) else Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 7.dp,
                        end = 7.dp,
                        top = 0.dp,
                        bottom = 7.dp
                    )),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            ),
            elevation = CardDefaults.cardElevation(
                if (viewModel.isBlurred.value && !isContextMenuVisible)
                    10.dp else 0.dp
            ),
        ) {
            Row(
                modifier = Modifier
                    .padding(5.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = text,
                    color = textColor,
                    fontFamily = monteBold,
                    fontSize = 15.sp,
                    softWrap = true
                )
            }
        }
        if (isUser) {
            ProfileImage(
                imageUrl = imageUrl,
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
        }
    }
    if (isContextMenuVisible) {
        Box {
            ContextMenu(isContextMenuVisible = isContextMenuVisible,
                offset = pressOffset.copy(
                    y = pressOffset.y - itemHeight
                ),
                onDismissRequest = {
                    isContextMenuVisible = false
                    viewModel.isBlurred.value = false
                })
        }
    }
}


@Composable
fun ContextMenu(
    isContextMenuVisible: Boolean,
    onDismissRequest: () -> Unit = {},
    offset: DpOffset
) {
    DropdownMenu(
        expanded = isContextMenuVisible,
        onDismissRequest = {
            onDismissRequest()
        },
        offset = offset,
        modifier = Modifier
            .background(appGradient)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 10.dp).background(appGradient)
        ) {
            ContextMenuCard(
                imageVector = Icons.Outlined.ContentCopy,
                contentDescription = "Copy"
            )
            ContextMenuCard(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete"
            )
            ContextMenuCard(
                imageVector = Icons.Outlined.IosShare,
                contentDescription = "Share"
            )
        }
    }
}

@Composable
fun ContextMenuCard(imageVector: ImageVector, contentDescription: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = textColor,
            modifier = Modifier
                .size(30.dp)
                .padding(bottom = 5.dp)
        )
        Text(
            text = contentDescription,
            color = textColor,
            fontFamily = monteSB,
            fontSize = 12.sp
        )

    }
}

@Composable
fun RepeatedCard(
    icon: Int,
    description: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 19.dp,
                end = 19.dp,
                top = 7.dp,
                bottom = 7.dp
            ),
        colors = CardDefaults.cardColors(
            containerColor = CardColor
        ),
        shape = RoundedCornerShape(40.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = description,
                tint = Color.Unspecified,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .size(35.dp)

            )
            Text(
                text = description,
                color = textColor,
                fontSize = 15.sp,
                fontFamily = monteNormal,
                softWrap = true
            )

        }

    }
}