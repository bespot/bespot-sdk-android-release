package com.bespot.sample

import android.app.Application
import com.bespot.sdk.Bespot
import com.bespot.sdk.sample.BuildConfig
import timber.log.Timber

class BespotSampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init Timber for debug logs
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Init Bespot SDK with the provided App Id and App Secret
        Bespot.init(this, BuildConfig.BESPOT_APP_ID, BuildConfig.BESPOT_APP_SECRET)

        // Enable Bespot SDK logs in DEBUG mode only
        Bespot.setLogsEnabled(BuildConfig.DEBUG)
    }
}
