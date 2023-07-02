package com.test.palmapi.repository

import android.util.Log
import com.test.palmapi.BuildConfig
import com.test.palmapi.dto.ApiPrompt
import com.test.palmapi.dto.Candidate
import com.test.palmapi.dto.PalmApi
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.url
import java.util.regex.Pattern

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    override suspend fun getApiData(apiPrompt: ApiPrompt): PalmApi {
        try {
            val a = client.post<PalmApi> {
                url("${ApiRoutes.BASE_URL}?key=${BuildConfig.API_KEY}")
                body = apiPrompt
                headers {
                    this.append("Content-Type", "application/json")
                }
            }
            return a
        } catch (e: Exception) {
            Log.i("ApiException", e.message.toString())
            return PalmApi(
                candidates = listOf(
                    Candidate(
                        output = extractMessageValue(e.message.toString()),
                        safetyRatings = null
                    )
                )
            )
        }
    }
}

private fun extractMessageValue(message: String): String? {
    val pattern = Pattern.compile("\"message\": \"(.*?)\",\"status\"")
    val matcher = pattern.matcher(message)
    return if (matcher.find()) {
        matcher.group(1)
    } else {
        "Something went wrong."
    }
}