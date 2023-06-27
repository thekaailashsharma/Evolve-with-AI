package com.test.palmapi.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.test.palmapi.database.accounts.Accounts
import com.test.palmapi.database.accounts.AccountsDao
import com.test.palmapi.database.chats.ChatDao
import com.test.palmapi.database.chats.ChatMessage

@Database(entities = [ChatMessage::class, Accounts::class], version = 1)
abstract class DatabaseObject : RoomDatabase() {

    abstract fun chatDao(): ChatDao
    abstract fun accountsDao(): AccountsDao

    companion object {
        @Volatile
        private var Instance: DatabaseObject? = null

        fun getInstance(context: Context): DatabaseObject {
            synchronized(this) {
                if (Instance == null) {
                    Instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            DatabaseObject::class.java,
                            "chatMessages"
                        ).build()
                }
            }
            return Instance!!
        }
    }
}