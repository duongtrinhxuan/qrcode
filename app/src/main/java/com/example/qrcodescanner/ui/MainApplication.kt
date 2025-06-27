package com.example.qrcodescanner.ui

import android.app.Application
import com.facebook.stetho.Stetho

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        // Initialize any libraries or components here if needed
    }

    // You can add other application-wide configurations or initializations here
}