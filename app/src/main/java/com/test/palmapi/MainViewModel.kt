package com.test.palmapi

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.palmapi.dto.ApiPrompt
import com.test.palmapi.dto.Candidate
import com.test.palmapi.dto.PalmApi
import com.test.palmapi.dto.Prompt
import com.test.palmapi.repository.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ApiService
) : ViewModel() {

    var apiData: MutableState<PalmApi?> = mutableStateOf(null)

    fun getApiData() {
        viewModelScope.launch {
            apiData = mutableStateOf(repository.getApiData(
                ApiPrompt(
                    prompt = Prompt(
                        text = "What is the weather in London?"
                    )
                )
            ))
        }
    }
}