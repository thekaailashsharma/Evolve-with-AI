package com.test.palmapi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.test.palmapi.navigation.NavController
import com.test.palmapi.ui.theme.PalmApiTheme
import com.test.palmapi.worker.WorkerClass
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var requestAccessibilityPermissionLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().subscribeToTopic("all")

        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<WorkerClass>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
        WorkManager.getInstance(this).enqueue(periodicWorkRequest)

//        requestAccessibilityPermissionLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (isAccessibilityPermissionGranted()) {
        setContent {
            var dl by remember{ mutableStateOf("") }
            var deepLink by remember{ mutableStateOf<Uri?>(null) }
            Firebase.dynamicLinks
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                    if (pendingDynamicLinkData != null) {
                        Log.i("DeeeeepLinks", dl)
                        deepLink = pendingDynamicLinkData.link
                        dl = pendingDynamicLinkData.link.toString()
                    }
                }
            PalmApiTheme {
                Log.i("DeeeeepLink", dl)
                NavController(dl)
            }
        }
//                } else {
//                    // Accessibility permission not granted, handle the failure case here
//                }
//            }


    }
//    override fun onResume() {
//        super.onResume()
//
//        if (!isAccessibilityPermissionGranted()) {
//            requestAccessibilityPermission()
//        }
//    }


}

