package com.test.palmapi.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.test.palmapi.R
import com.test.palmapi.datastore.UserDatastore
import com.test.palmapi.dto.Notify
import com.test.palmapi.dto.NotifyReturn
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class WorkerClass(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {


    override fun doWork(): Result {
        Log.d("WorkerClass", "It's Working")
        val datastore = UserDatastore(context = applicationContext)
        val job = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.IO + job)
        val httpClient = HttpClient(CIO) {
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
        val apiEndpoint =
            "http://10.182.0.191:3000/api"

        val result = scope.async {
            if (datastore.getStore.first() == "") {
                Result.failure()
                return@async Result.failure()
            } else {
                val text = runBlocking {
                    datastore.getStore.first()
                }
                val email = runBlocking { datastore.getEmail.first() }
                try {
                    val response = runBlocking {
                        httpClient.post<NotifyReturn> {
                            url(apiEndpoint)
                            body = Notify(
                                date = System.currentTimeMillis(),
                                userId = email,
                                text = text

                            )
                            headers {
                                this.append("Content-Type", "application/json")
                            }
                        }
                    }
                    datastore.storeText("")
                    Result.success()
                } catch (e: Exception) {
                    Log.i("Messagee", e.message.toString())
                    Log.i("Messagee", text)
                    Result.failure()
                }

            }
        }
        return result.getCompleted()
    }
}
