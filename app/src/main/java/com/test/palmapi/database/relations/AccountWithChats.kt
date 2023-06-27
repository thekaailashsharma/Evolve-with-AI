package com.test.palmapi.database.relations

import android.accounts.Account
import androidx.room.Embedded
import androidx.room.Relation
import com.test.palmapi.database.chats.ChatMessage

data class AccountWithChats(
    @Embedded val account: Account,
    @Relation(
        parentColumn = "id",
        entityColumn = "time"
    )
    val chats: List<ChatMessage>
)