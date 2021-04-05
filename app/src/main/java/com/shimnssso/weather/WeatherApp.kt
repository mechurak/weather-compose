package com.shimnssso.weather

import android.app.Application
import timber.log.Timber

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}