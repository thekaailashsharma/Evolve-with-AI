package com.test.palmapi.database


import com.test.palmapi.database.accounts.Accounts
import com.test.palmapi.database.accounts.AccountsDao
import com.test.palmapi.database.chats.ChatDao
import com.test.palmapi.database.chats.ChatMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DatabaseRepo(private val chatDao: ChatDao, private val accountsDao: AccountsDao) {
    val allAccounts: Flow<List<Accounts>> = accountsDao.getAccount()
    fun getType(id: String): Flow<Accounts> = accountsDao.getAccountById(uniqueId = id)
    fun allMessages(id: String): Flow<List<ChatMessage>> = chatDao.getAllChatMessages(id = id)
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    fun insertMessage(chatMessage: ChatMessage) {
        coroutineScope.launch {
            chatDao.insertChatMessage(chatMessage)
        }
    }

    fun updateCurrentAccount(isCurrent: Boolean, uniqueId: String) {
        coroutineScope.launch {
            accountsDao.updateCurrentAccount(isCurrent, uniqueId)
        }
    }

    fun removeCurrentAccount() {
        coroutineScope.launch {
            accountsDao.removeCurrentAccount()
        }
    }

    fun insertAccount(account: Accounts) {
        coroutineScope.launch {
            accountsDao.insertAccount(account)
        }
    }

    fun saveChatMessage(name: String, previousName: String = "New Chat", isPined: Boolean) {
        coroutineScope.launch {
            chatDao.saveChatMessage(name, previousName, isPined)
        }
    }

    fun getSavedMessage(name: String) = chatDao.getSavedMessages(name)
    fun getUniqueSaved() = chatDao.getUniqueSaved()

    fun deleteMessage(time: Long) {
        coroutineScope.launch {
            chatDao.deleteChatMessage(time)
        }
    }

}