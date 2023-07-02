package com.test.palmapi

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.test.palmapi.database.DatabaseRepo
import com.test.palmapi.database.accounts.Accounts
import com.test.palmapi.database.chats.ChatMessage
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.dto.ApiPrompt
import com.test.palmapi.dto.PalmApi
import com.test.palmapi.dto.Prompt
import com.test.palmapi.login.SignInResult
import com.test.palmapi.login.SignInState
import com.test.palmapi.repository.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: ApiService,
    private val dbRepository: DatabaseRepo
) : AndroidViewModel(application) {

    var apiData: MutableState<PalmApi?> = mutableStateOf(null)
    val listOfMessages: MutableState<MutableList<ChatMessage>> = mutableStateOf(mutableListOf())
    val message: MutableState<String> = mutableStateOf("")
    fun allMessages(uid: String): Flow<List<ChatMessage>> = dbRepository.allMessages(uid)
    fun getType(uid: String) = dbRepository.getType(uid)
    fun savedMessages(savedName: String): Flow<List<ChatMessage>> =
        dbRepository.getSavedMessage(savedName)

    fun getUniqueSaved() = dbRepository.getUniqueSaved()
    var isBlurred: MutableState<Boolean> = mutableStateOf(false)
    var savedName: MutableState<String> = mutableStateOf("")
    var isSaved: MutableState<Boolean> = mutableStateOf(false)
    var uid: MutableState<String> = mutableStateOf("")
    val allAccounts: Flow<List<Accounts>>
    val datastore = UserDatastore(application.applicationContext)

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            datastore.getUID.collectLatest {
                uid.value = it
            }
        }
        allAccounts = dbRepository.allAccounts
    }

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun addAccount(
        firstName: String,
        lastName: String,
        email: String,
        photoUrl: String,
        uniqueId: String,
        isCurrent: Boolean = false,
        type: String
    ) {
        viewModelScope.launch {
            Log.i("Type", type)
            dbRepository.insertAccount(
                Accounts(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    photoUrl = photoUrl,
                    uniqueId = uniqueId,
                    isCurrent = isCurrent,
                    type = type
                )
            )
        }
    }


    fun getApiData() {
        viewModelScope.launch {
            Log.i("Messages API Called Before", listOfMessages.value.toString())
            delay(1000)
            apiData = mutableStateOf(
                repository.getApiData(
                    ApiPrompt(
                        prompt = Prompt(
                            text = message.value
                        )
                    )
                )
            )
            insertChat(
                ChatMessage(
                    time = System.currentTimeMillis(),
                    message = apiData.value?.candidates?.get(0)?.output ?: "Something went wrong.",
                    isUser = false,
                    uID = uid.value
                )
            )
            Log.i("Messages API Called", listOfMessages.value.toString())
        }
    }


    fun insertChat(chatMessage: ChatMessage) {
        viewModelScope.launch {
            dbRepository.insertMessage(chatMessage)
        }
    }

    fun updateCurrentAccount(isCurrent: Boolean, uniqueId: String) {
        viewModelScope.launch {
            dbRepository.updateCurrentAccount(isCurrent, uniqueId)
        }
    }

    fun removeCurrentAccount() {
        viewModelScope.launch {
            dbRepository.removeCurrentAccount()
        }
    }

    fun saveChatMessage(name: String, previousName: String = "New Chat", isPined: Boolean) {
        viewModelScope.launch {
            dbRepository.saveChatMessage(name, previousName, isPined)
        }
    }

    fun deleteChat(time: Long) {
        viewModelScope.launch {
            dbRepository.deleteMessage(time)
        }
    }
}
