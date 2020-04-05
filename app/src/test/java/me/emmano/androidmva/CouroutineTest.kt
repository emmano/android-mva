package me.emmano.androidmva

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import me.emmano.androidmva.base.TestFlow
import me.emmano.androidmva.rule.CoroutineTestRule
import org.junit.Rule

@ExperimentalCoroutinesApi
interface CoroutineTest {

    @get:Rule
    val coroutineRule: CoroutineTestRule

    fun test(
        test: suspend TestCoroutineDispatcher.() -> Unit) = coroutineRule.testDispatcher.runBlockingTest { test(coroutineRule.testDispatcher) }

    fun <T> Flow<T>.test(): TestFlow<T> = TestFlow(this, coroutineRule.testDispatcher)


}