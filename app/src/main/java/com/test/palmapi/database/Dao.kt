package com.test.palmapi.database

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

    @Query("SELECT * FROM chat_message")
    fun getAllChatMessages(): Flow<List<ChatMessage>>

}