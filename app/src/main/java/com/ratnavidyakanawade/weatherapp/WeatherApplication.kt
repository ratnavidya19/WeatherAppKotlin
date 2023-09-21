package com.ratnavidyakanawade.weatherapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //better logging
        Timber.plant(Timber.DebugTree())
    }
}