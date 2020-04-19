package me.emmano.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun <T> test(block: suspend (CoroutineScope) -> T) {
    runBlocking(SynchronousDispatcher()) {
        block(this)
    }

}
