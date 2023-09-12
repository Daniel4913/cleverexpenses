package com.example.cleverex

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.Forest.plant


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        plant(Timber.DebugTree())

        FirebaseApp.initializeApp(this)

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}