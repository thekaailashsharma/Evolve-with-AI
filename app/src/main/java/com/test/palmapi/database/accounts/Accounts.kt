package com.test.palmapi.database.accounts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Accounts(
    val firstName: String,
    val lastName: String?,
    val email: String,
    val photoUrl: String?,
    @PrimaryKey(autoGenerate = false)
    val uniqueId: String,
    val isCurrent: Boolean = false,
    val type: String
)
