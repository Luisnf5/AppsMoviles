package com.example.tragomaestro

import android.app.Application
import com.example.tragomaestro.database.TragoMaestroDatabase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import timber.log.Timber

class TragoMaestroApplication : Application() {

    val database: TragoMaestroDatabase by lazy {
        TragoMaestroDatabase.getDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        val appCheck = FirebaseAppCheck.getInstance()
        appCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )

        Timber.plant(Timber.DebugTree())
        Timber.i("TragoMaestroApplication inicializada")
    }
}