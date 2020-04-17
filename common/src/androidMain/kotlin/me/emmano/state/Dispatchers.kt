package me.emmano.state

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual object Dispatchers {
    actual val dispatcher: CoroutineDispatcher = Dispatchers.Main
}