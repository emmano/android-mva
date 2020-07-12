package me.emmano.androidmva.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import me.emmano.androidmva.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class MvaApplicaton : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

    }
}