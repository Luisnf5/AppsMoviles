package com.example.tragomaestro

import android.app.Application
import timber.log.Timber

class TragoMaestroApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Timber inicializado en TragoMaestroApplication")
    }
}