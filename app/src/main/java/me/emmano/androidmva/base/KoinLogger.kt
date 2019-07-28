package me.emmano.androidmva.base

import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import timber.log.Timber

class KoinLogger : Logger() {
    override fun log(level: Level, msg: MESSAGE) {
        Timber.tag(msg)
    }
}