package com.test.palmapi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_message")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    val time: Long,
    @ColumnInfo(name = "message")
    val message: String?,
    @ColumnInfo(name = "is_user")
    val isUser: Boolean
)