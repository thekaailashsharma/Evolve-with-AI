package com.test.palmapi.imeService

import android.content.Context
import com.test.palmapi.BuildConfig
import com.test.palmapi.dto.ApiPrompt
import com.test.palmapi.dto.PalmApi
import com.test.palmapi.dto.Prompt
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.url
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class Action(
    val name: String,
    val prompt: String,
)

data class UseAction(
    val action: String,
    val onClick: (Context, Action, String) -> Unit
)

val useActions = listOf(
    UseAction("Use text") { ctx: Context, _: Action, response: String ->
        (ctx as IMEService).currentInputConnection.commitText(
            "\n$response",
            response.length
        )
    },
    UseAction("Regenerate") { _: Context, action: Action, response: String ->
        GlobalScope.launch(Dispatchers.IO) {
            callAPIv2(action = action, text = response)
        }
    }
)

val actions = listOf(
    Action(
        "Summarize",
        "You are an expert summarizer. " +
                "Summarize the given text: "
    ),
    Action(
        "Recommend",
        "Provide recommendations based on user input :"
    ),
    Action(
        "Generate",
        "Generate content based on user input:  "

    ),
    Action(
        "Provide",
        "Provide relevant information or resources on the following input: {}"

    )
)


private val httpClient = HttpClient(CIO) {
    install(Logging)
    install(WebSockets)
    install(JsonFeature) {
        this.serializer = GsonSerializer() {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 300000 // Increase the timeout value as needed
    }
}
private val apiEndpoint =
    "https://generativelanguage.googleapis.com/v1beta2/models/text-bison-001:generateText?key=${BuildConfig.API_KEY}"

@OptIn(DelicateCoroutinesApi::class)
suspend fun callAPI(text: String?): String? = suspendCoroutine { continuation ->
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val response = httpClient.post<PalmApi> {
                url(apiEndpoint)
                body = ApiPrompt(
                    prompt = Prompt(
                        text = text
                    )
                )
                headers {
                    this.append("Content-Type", "application/json")
                }
            }
            val result = response.candidates?.get(0)?.output
            continuation.resume(result)
        } catch (e: Exception) {
            continuation.resume(null)
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun callAPIv2(action: Action, text: String): String? = suspendCoroutine { continuation ->
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val response = httpClient.post<PalmApi> {
                url(apiEndpoint)
                body = ApiPrompt(
                    prompt = Prompt(
                        text = "${action.prompt} + $text"
                    )
                )
                headers {
                    this.append("Content-Type", "application/json")
                }
            }
            val result = response.candidates?.get(0)?.output
            continuation.resume(result)
        } catch (e: Exception) {
            continuation.resume(null)
        }
    }
}

