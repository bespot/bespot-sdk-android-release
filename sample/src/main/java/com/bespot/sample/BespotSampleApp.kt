package com.bespot.sample

import android.app.Application
import com.bespot.sdk.Bespot
import com.bespot.sdk.BuildConfig

class BespotSampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Init Bespot SDK with the provided App Id and App Secret
        Bespot.init(this, "your_app_id", "your_secret")

        // Enable Bespot SDK logs in DEBUG mode only
        Bespot.setLogsEnabled(BuildConfig.DEBUG)
    }
}