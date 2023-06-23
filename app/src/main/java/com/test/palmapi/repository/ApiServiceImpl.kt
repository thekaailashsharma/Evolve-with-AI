package com.test.palmapi.repository

import com.test.palmapi.dto.ApiPrompt
import com.test.palmapi.dto.Candidate
import com.test.palmapi.dto.PalmApi
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.url

class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {

    override suspend fun getApiData(apiPrompt: ApiPrompt): PalmApi {
        val a = client.post<PalmApi> {
            url("${ApiRoutes.BASE_URL}?key=AIzaSyD3Uedh-W7B2T9BHuls61gMBtGMrgtwei8")
            body = apiPrompt
            headers {
                this.append("Content-Type", "application/json")
            }
        }
        return a
    }
}