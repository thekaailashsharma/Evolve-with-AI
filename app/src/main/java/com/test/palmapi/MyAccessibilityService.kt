package com.test.palmapi

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
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

private const val NOTIFICATION_ID = 1

// Define a unique channel ID for the notification channel
private const val CHANNEL_ID = "MyNotificationChannel"
private const val CHANNEL_NAME = "My Notification Channel"

class MyAccessibilityService : AccessibilityService() {

    private lateinit var clipboardManager: ClipboardManager
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
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        var result: String? = ""
        Log.i("Accessibility", "onAccessibilityEvent")
        try {
            Log.i("Accessibility", "source: ${event?.source}")
            val text = extractMyMatchValue(event?.text?.get(0).toString())
            Log.i("Accessibility", "event: ${event?.text}")
            if (text != null) {
                Log.i("Accessibility", "text: $text")
                fireNotification(this, "PalmAPI", "Generating text...")
                GlobalScope.launch(Dispatchers.IO) {
                    result = callAPI(text)
                    Log.i("Accessibility", "result: $result")
                    result?.let { copyToClipboard(it) }
                    fireNotificationCopy(
                        this@MyAccessibilityService,
                        "Text generated!",
                        result ?: "No text generated"
                    )
                }

            }

        } catch (e: Exception) {
            Log.i("Accessibility", "Exception: ${e.message}")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun callAPI(text: String?): String? = suspendCoroutine { continuation ->
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
                Log.i("Accessibility", "response: ${response.candidates}")
            } catch (e: Exception) {
                Log.i("Accessibility", "Exception: ${e.message}")
                continuation.resume(null)
            }
        }
    }


    override fun onServiceConnected() {
        // Set the type of events that this service wants to listen to. Others
        // won't be passed to this service.
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val info = AccessibilityServiceInfo()
        info.description
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED or
                AccessibilityEvent.TYPE_VIEW_FOCUSED or
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED


        // Set the type of feedback your service will provide.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
        info.notificationTimeout = 100
        serviceInfo = info
        Log.i("Accessibility", "onServiceConnected")
    }


    override fun onInterrupt() {
        println("Service Interrupted")
    }

    private fun extractMyMatchValue(input: String): String? {
        val regexPattern =
            "\\{\\{(?<MyMatch>.*?)\\}\\}".toRegex(option = RegexOption.DOT_MATCHES_ALL)
        val match = regexPattern.find(input)
        Log.i("Accessibility", "match: $match")
        return match?.groups?.get("MyMatch")?.value
    }

    private fun copyToClipboard(text: CharSequence) {
        val clipData = ClipData.newPlainText("Label", text)
        clipboardManager.setPrimaryClip(clipData)
    }


    // Create a notification channel for Android Oreo and higher
    private fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }

    @SuppressLint("NotificationPermission")
    fun showCopy(context: Context, title: String, message: String) {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .addAction(
                R.drawable.appicon,
                "Copy",
                createCopyActionPendingIntent(context, message)
            )

        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    // Create and display the notification
    @SuppressLint("NotificationPermission")
    private fun showStart(context: Context, title: String, message: String) {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.appicon)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager?.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    // Call this function in your AccessibilityService after the task is completed
    private fun fireNotification(context: Context, title: String, message: String) {
        createNotificationChannel(context)
        showStart(context, title, message)
    }

    private fun fireNotificationCopy(context: Context, title: String, message: String) {
        createNotificationChannel(context)
        showCopy(context, title, message)
    }

    private fun createCopyActionPendingIntent(context: Context, message: String) =
        NotificationActionHelper.createCopyPendingIntent(context, message)

    object NotificationActionHelper {
        private const val COPY_ACTION = "com.your.package.ACTION_COPY"
        private const val COPY_ACTION_REQUEST_CODE = 101

        fun createCopyPendingIntent(context: Context, text: String): PendingIntent {
            val copyIntent = Intent().apply {
                putExtra("text", text)
            }

            val broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action == COPY_ACTION) {
                        val clipboard =
                            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val copiedText = intent.getStringExtra("text")
                        clipboard.setPrimaryClip(ClipData.newPlainText("Copied Text", copiedText))
                        Toast.makeText(context, "Text copied to clipboard", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            context.registerReceiver(broadcastReceiver, IntentFilter(COPY_ACTION))

            return PendingIntent.getBroadcast(
                context,
                COPY_ACTION_REQUEST_CODE,
                copyIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }


}
