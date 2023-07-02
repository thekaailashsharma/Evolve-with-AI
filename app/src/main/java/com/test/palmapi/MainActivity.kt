package com.test.palmapi

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import com.test.palmapi.navigation.NavController
import com.test.palmapi.ui.theme.PalmApiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var requestAccessibilityPermissionLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestAccessibilityPermissionLauncher =
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (isAccessibilityPermissionGranted()) {
                    setContent {
                        PalmApiTheme {
                            NavController()
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

