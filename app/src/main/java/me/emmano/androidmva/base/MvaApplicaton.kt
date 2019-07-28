package me.emmano.androidmva.base

import android.app.Application
import me.emmano.androidmva.BuildConfig
import me.emmano.androidmva.comics.comicsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MvaApplicaton : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        startKoin {
            logger(KoinLogger())

            androidContext(this@MvaApplicaton)

            modules(listOf(networkModule, comicsModule))
        }

    }
}