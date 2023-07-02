package com.test.palmapi.database.accounts

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Accounts)

    @Query("SELECT * FROM accounts")
    fun getAccount(): Flow<List<Accounts>>

    @Query("SELECT * FROM accounts WHERE uniqueId = :uniqueId")
    fun getAccountById(uniqueId: String): Flow<Accounts>

    @Query("Update accounts set isCurrent = :isCurrent where uniqueId = :uniqueId")
    suspend fun updateCurrentAccount(isCurrent: Boolean, uniqueId: String)

    @Query("Update accounts set isCurrent = 0")
    suspend fun removeCurrentAccount()

}

