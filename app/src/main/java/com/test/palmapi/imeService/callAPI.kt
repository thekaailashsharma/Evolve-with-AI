package com.test.palmapi.imeService

import android.content.Context
import android.util.Log
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

val useActions = listOf(
    Action("Use text", ""),
    Action("Regenerate", "")
)

val translateActions = listOf(
    Action(
        "Hindi",
        "You are an expert translator" +
                "Translate the given text to Hindi: "
    ),
    Action(
        "Marathi",
        "You are an expert translator" +
                "Translate the given text to Marathi: "
    ),
    Action(
        "Gujarati",
        "You are an expert translator" +
                "Translate the given text to Gujarati: "
    ),
    Action(
        "Kannada",
        "You are an expert translator" +
                "Translate the given text to Kannada: "
    ),
    Action(
        "Tamil",
        "You are an expert translator" +
                "Translate the given text to Tamil: "
    ),
    Action(
        "French",
        "You are an expert translator" +
                "Translate the given text to French: "
    ),
    Action(
        "Spanish",
        "You are an expert translator" +
                "Translate the given text to Spanish: "
    ),
)

val actions = listOf(
    Action(
        "Summarize",
        "Summarize this paragraph and detail some relevant context.\n" +
                "\n" +
                "Text: "
    ),
    Action(
        "Generate",
        "You are an expert content curator." +
                "Generate content based on user input:  "

    ),
    Action(
        "Provide",
        "Provide relevant information or resources on the following input: "

    ),
    Action(
        "Recommend",
        "Provide recommendations based on user input :"
    ),
    Action(
        "Translate (Beta)",
        "You are an expert translator" +
                "Translate the given text to "

    ),
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
                Log.i("Text going is ", "${action.prompt} + {$text} ")
                url(apiEndpoint)
                body = ApiPrompt(
                    prompt = Prompt(
                        text = "${action.prompt} + {$text} "
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

