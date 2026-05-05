package com.example.tragomaestro

import android.app.Application
import com.example.tragomaestro.database.TragoMaestroDatabase
import timber.log.Timber

class TragoMaestroApplication : Application() {

    val database: TragoMaestroDatabase by lazy {
        TragoMaestroDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("TragoMaestroApplication inicializada")
    }
}