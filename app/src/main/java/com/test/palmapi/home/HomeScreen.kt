package com.test.palmapi.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.test.palmapi.MainViewModel
import com.test.palmapi.newChat.ChatCard
import com.test.palmapi.ui.theme.appGradient
import com.test.palmapi.utils.CollapsedTopBarHomeScreen
import com.test.palmapi.utils.ExpandedTopBarHomeScreen
import com.test.palmapi.utils.NavigationDrawer
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    photoUrl: String,
    name: String,
    email: String
) {
    val listState = rememberLazyListState()
    val isCollapsed: Boolean by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    ModalNavigationDrawer(drawerContent = {
        NavigationDrawer(drawerState, photoUrl, name, email)
    }, drawerState = drawerState, gesturesEnabled = drawerState.isOpen) {
        Scaffold(
            topBar = {
                CollapsedTopBarHomeScreen(
                    imageUrl = photoUrl,
                    isCollapsed = isCollapsed,
                    scroll = listState
                )
            },
        ) { padding ->
            println(padding)
            CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                val result by viewModel.allMessages.collectAsState(initial = listOf())
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(appGradient),
                    state = listState
                ) {
                    item {
                        ExpandedTopBarHomeScreen(
                            imageUrl = photoUrl,
                            textValue = "",
                            onTextChange = {},
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
                    item { Spacer(modifier = Modifier.height(20.dp)) }
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

