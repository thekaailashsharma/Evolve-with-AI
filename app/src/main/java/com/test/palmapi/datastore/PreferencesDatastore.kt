package com.test.palmapi.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.datastore: DataStore<Preferences> by preferencesDataStore("pref")

class UserDatastore(private val context: Context) {
    companion object {
        val userEmail = stringPreferencesKey("userEmail")
        val userPfp = stringPreferencesKey("userPfp")
        val userName = stringPreferencesKey("userName")
        val uniqueId = stringPreferencesKey("uniqueId")
        val storedText = stringPreferencesKey("store")
    }

    val getEmail: Flow<String> = context.datastore.data.map {
        it[userEmail] ?: ""
    }

    suspend fun saveEmail(email: String) {
        context.datastore.edit {
            it[userEmail] = email
        }
    }

    val getUID: Flow<String> = context.datastore.data.map {
        it[uniqueId] ?: ""
    }

    suspend fun saveUID(uid: String) {
        context.datastore.edit {
            it[uniqueId] = uid
        }
    }

    val getPfp: Flow<String> = context.datastore.data.map {
        it[userPfp] ?: ""
    }

    suspend fun savePfp(pfp: String) {
        context.datastore.edit {
            it[userPfp] = pfp
        }
    }

    val getStore: Flow<String> = context.datastore.data.map {
        it[storedText] ?: ""
    }

    suspend fun storeText(text: String) {
        context.datastore.edit {
            it[storedText] = text
        }
    }

    val getName: Flow<String> = context.datastore.data.map {
        it[userName] ?: ""
    }

    suspend fun saveName(name: String) {
        context.datastore.edit {
            it[userName] = name
        }
    }

}