package com.example.cloudservices

import android.app.Application
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import java.io.File

class CloudServicesApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        Configuration.getInstance().load(this, prefs)


        Configuration.getInstance().userAgentValue = packageName


        Configuration.getInstance().osmdroidBasePath = File(filesDir, "osmdroid")
        Configuration.getInstance().osmdroidTileCache = File(filesDir, "osmdroid/tiles")
    }
}
