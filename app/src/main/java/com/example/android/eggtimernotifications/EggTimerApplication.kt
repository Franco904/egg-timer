package com.example.android.eggtimernotifications

import android.app.Application
import com.google.firebase.FirebaseApp

class EggTimerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }
}