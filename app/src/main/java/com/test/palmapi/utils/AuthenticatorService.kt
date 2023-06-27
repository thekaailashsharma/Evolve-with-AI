package com.test.palmapi.utils

import android.app.Service
import android.content.Intent
import android.os.IBinder

class CustomAccountAuthenticatorService : Service() {
    private lateinit var authenticator: CustomAccountAuthenticator

    override fun onCreate() {
        super.onCreate()
        authenticator = CustomAccountAuthenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return authenticator.iBinder
    }
}