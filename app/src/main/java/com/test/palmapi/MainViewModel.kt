package com.test.palmapi

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.test.palmapi.database.DatabaseObject
import com.test.palmapi.database.DatabaseRepo
import com.test.palmapi.database.chats.ChatMessage
import com.test.palmapi.dto.ApiPrompt
import com.test.palmapi.dto.PalmApi
import com.test.palmapi.dto.Prompt
import com.test.palmapi.repository.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: ApiService
) : AndroidViewModel(application) {

    var apiData: MutableState<PalmApi?> = mutableStateOf(null)
    val listOfMessages: MutableState<MutableList<ChatMessage>> = mutableStateOf(mutableListOf())
    val message: MutableState<String> = mutableStateOf("")
    val allMessages: Flow<List<ChatMessage>>
    val savedMessages: Flow<List<ChatMessage>>
    var isBlurred: MutableState<Boolean> = mutableStateOf(false)
    var savedName: MutableState<String> = mutableStateOf("")


    private val dbRepository: DatabaseRepo

    init {
        val dB = DatabaseObject.getInstance(application)
        val dataDao = dB.hisDao()
        dbRepository = DatabaseRepo(dataDao)
        allMessages = dbRepository.allMessages
        savedMessages = dbRepository.getSavedMessage(savedName.value)
    }

    fun getApiData() {
        viewModelScope.launch {
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

    fun saveChatMessage(name: String, isPined: Boolean) {
        viewModelScope.launch {
            dbRepository.saveChatMessage(name, isPined)
        }
    }

    fun deleteChat(time: Long) {
        viewModelScope.launch {
            dbRepository.deleteMessage(time)
        }
    }
}
