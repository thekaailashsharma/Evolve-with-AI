package com.test.palmapi.database.chats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_message")
data class ChatMessage(
    @PrimaryKey(autoGenerate = false)
    val time: Long,
    @ColumnInfo(name = "message")
    val message: String?,
    @ColumnInfo(name = "isUser")
    val isUser: Boolean,
    @ColumnInfo(name = "Name")
    val name: String = "New Chat",
    @ColumnInfo(name = "isPined")
    val isPined: Boolean = false,
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "uID")
    val uID: String,
)