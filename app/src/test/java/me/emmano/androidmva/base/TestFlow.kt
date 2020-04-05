package me.emmano.androidmva.base

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert

@ExperimentalCoroutinesApi
class TestFlow<T>(private val flow: Flow<T>,
                  private val testDispatcher: TestCoroutineDispatcher) {

    private val emissions = mutableListOf<T>()
    private val scope = CoroutineScope(testDispatcher)
    init {
            scope.launch {
                flow.toList(emissions)
            }
    }

    infix fun assertFirstEmitted(value: T)  =
        MatcherAssert.assertThat(value, CoreMatchers.equalTo(emissions.first()))


    fun assertEmitted(vararg value: T) {
            MatcherAssert.assertThat(emissions, CoreMatchers.equalTo(listOf(*value)))
    }

    fun assertValueAt(position: Int, value: T) =
        MatcherAssert.assertThat(emissions[position], CoreMatchers.equalTo(value))

}
