package com.test.palmapi.database


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DatabaseRepo(private val chatDao: ChatDao) {
    val allMessages: Flow<List<ChatMessage>> = chatDao.getAllChatMessages()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun insertHistory(chatMessage: ChatMessage) {
        coroutineScope.launch {
            chatDao.insertChatMessage(chatMessage)
        }
    }

}