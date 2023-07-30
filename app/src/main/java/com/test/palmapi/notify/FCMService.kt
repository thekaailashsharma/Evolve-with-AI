package com.test.palmapi.notify


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.test.palmapi.MainActivity
import com.test.palmapi.MyAccessibilityService
import com.test.palmapi.R
import java.io.IOException
import java.net.URL


class FirebaseInstanceIDService : FirebaseMessagingService() {


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        println("Token is $token")
    }


    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null) {
            Log.i("Received Notification", message.notification?.title ?: "")
            println("Message Image is ${message.data["url"]}")
            try {
                val url = URL(message.data["url"])
                val prompt = message.data["prompt"]
                val emotion = message.data["emotion"]
                val images: Bitmap? =
                    BitmapFactory.decodeStream(url.openConnection().getInputStream())
                generate(
                    title = message.notification?.title,
                    message = message.notification?.body,
                    image = images,
                    prompt = prompt,
                    emotion = emotion
                )

            } catch (e: IOException) {
                println()
            }
        }
    }

    private fun generate(
        title: String?,
        message: String?,
        image: Bitmap?,
        prompt: String? = null,
        emotion: String? = null
    ) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        try {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val dL = FirebaseDynamicLinks.getInstance().dynamicLink {
                link = Uri.parse("https://palmapi.page.link/prompt?contentt=$prompt+emotion=$emotion")
                domainUriPrefix = "https://palmapi.page.link"
            }

            val dynamicLink = dL.uri
            intent.data = dynamicLink
            val pending: PendingIntent = TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(100, PendingIntent.FLAG_IMMUTABLE)
            }
            val channelId = "Default"
            val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(com.test.palmapi.R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message).setAutoCancel(true)
                .setContentIntent(pending)
                .setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(image).setBigContentTitle(title)
                )
                .addAction(
                    R.drawable.appicon,
                    "Copy",
                    createCopyActionPendingIntent(this, prompt ?: "")
                )
                .setPriority(Notification.PRIORITY_MAX)
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Default channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                manager.createNotificationChannel(channel)
            }
            manager.notify(0, builder.build())
        } catch (e: IOException) {
            println(e)
        }

    }
}

private fun createCopyActionPendingIntent(context: Context, message: String) =
    MyAccessibilityService.NotificationActionHelper.createCopyPendingIntent(context, message)


