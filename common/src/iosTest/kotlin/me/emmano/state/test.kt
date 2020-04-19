package me.emmano.state

import kotlinx.coroutines.*

@OptIn(InternalCoroutinesApi::class)
actual fun <T> test(block: suspend (CoroutineScope) -> T) {
    runBlocking(SynchronousDispatcher()) {
        block(this)
    }
}
