package me.emmano.state

/**
 * Copyright 2020, White Cloak Technologies, Inc., All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/01/2020
 */

import io.ktor.utils.io.core.Closeable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.*
import me.emmano.state.Dispatchers.dispatcher

fun <T> ConflatedBroadcastChannel<T>.wrap(): CFlow<T> = CFlow(asFlow())

fun <T> Flow<T>.wrap(): CFlow<T> = CFlow(this)

class CFlow<T>(private val origin: Flow<T>) : Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job(/*ConferenceService.coroutineContext[Job]*/)

        onEach {
            block(it)
        }.launchIn(CoroutineScope(dispatcher + job))

        return object : Closeable {
            override fun close() {
                job.cancel()
            }
        }
    }
}