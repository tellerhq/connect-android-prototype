package io.teller.connect

import android.app.Application
import timber.log.Timber.*
import timber.log.Timber.Forest.plant

class TellerConnectApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
    }
}