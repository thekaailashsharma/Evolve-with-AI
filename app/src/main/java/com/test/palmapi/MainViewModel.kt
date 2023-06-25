package com.test.palmapi

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.test.palmapi.database.ChatMessage
import com.test.palmapi.database.DatabaseObject
import com.test.palmapi.database.DatabaseRepo
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
    var isLoading: MutableState<Boolean> = mutableStateOf(false)
    var isBlurred: MutableState<Boolean> = mutableStateOf(false)

    private val dbRepository: DatabaseRepo

    init {
        val dB = DatabaseObject.getInstance(application)
        val dataDao = dB.hisDao()
        dbRepository = DatabaseRepo(dataDao)
        allMessages = dbRepository.allMessages
    }

    fun getApiData() {
        isLoading = mutableStateOf(true)
        viewModelScope.launch {
            apiData = mutableStateOf(
                repository.getApiData(
                    ApiPrompt(
                        prompt = Prompt(
                            text = message.value
                        )
                    )
                )
            )
            saveChat(ChatMessage(
                time = System.currentTimeMillis(),
                message = apiData.value?.candidates?.get(0)?.output,
                isUser = false
            ))

            Log.i("Messages API Called", listOfMessages.value.toString())
            delay(2000)
            isLoading = mutableStateOf(false)
        }
    }

    fun saveChat(chatMessage: ChatMessage){
        viewModelScope.launch {
            dbRepository.insertHistory(chatMessage)
        }
    }
}
