package com.konbini.inseadqr

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@HiltAndroidApp
class MainApplication : Application() {
    companion object {
        const val TAG = "MainApplication"
        lateinit var instance: MainApplication

        fun shared(): MainApplication {
            return instance
        }
    }

    override fun onCreate() {
        try {
            logLogcat(this)
        } catch (e: IOException) {
           // LogsUtil.writeToFile(baseContext, "Exception caught: $e")
        }
        super.onCreate()
    }

    init {
        this.also { instance = it }
    }

    private fun logLogcat(context: Context) {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendarTime = Calendar.getInstance().time
        val currentDate = formatter.format(calendarTime)
        val filename = currentDate + "_Logcat.txt"
        val outputFile = File(context.externalCacheDir, filename)
        Runtime.getRuntime().exec("logcat -f " + outputFile.absolutePath + " *:W")
    }
}