package com.test.palmapi.database.chats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(chatMessage: ChatMessage)

    @Query("DELETE FROM chat_message WHERE time = :time")
    suspend fun deleteChatMessage(time: Long)

    @Query("SELECT * FROM chat_message where name = :name")
    fun getAllChatMessages(name: String = "New Chat"): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_message where name = :name")
    fun getSavedMessages(name: String): Flow<List<ChatMessage>>

    @Query("Update chat_message set name = :name, isPined = :isPined where name = 'New Chat'")
    suspend fun saveChatMessage(name: String, isPined: Boolean)


}