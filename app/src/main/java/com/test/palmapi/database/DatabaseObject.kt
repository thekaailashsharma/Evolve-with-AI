package com.test.palmapi.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChatMessage::class], version = 1)
abstract class DatabaseObject : RoomDatabase() {

    abstract fun hisDao(): ChatDao

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
                        )
                            .build()
                }
            }
            return Instance!!
        }
    }
}