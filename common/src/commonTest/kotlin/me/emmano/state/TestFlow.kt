package me.emmano.state

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.ContinuationInterceptor
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class TestFlow<T>(private val flow: Flow<T>, scope: CoroutineScope) {

    private val emissions = mutableListOf<T>()
    init {
            CoroutineScope(scope.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher + Job()).launch {
                flow.toList(emissions)
            }
    }

     fun assertEmitted(vararg value: T) {
            assertEquals(listOf(*value), emissions)
    }

    fun assertValueAt(position: Int, value: T) = assertEquals(value, emissions[position])
}
