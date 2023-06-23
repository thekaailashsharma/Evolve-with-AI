package com.test.palmapi.repository

import com.test.palmapi.dto.ApiPrompt
import com.test.palmapi.dto.Candidate
import com.test.palmapi.dto.PalmApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging


interface ApiService {
    suspend fun getApiData(apiPrompt: ApiPrompt): PalmApi

}