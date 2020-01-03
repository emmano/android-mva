package me.emmano.androidmva.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.*
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import kotlin.IllegalStateException


class BaseViewModelTest : BaseTest() {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `initial state is broadcasted`() = test {
        val observer = mock<Observer<in TestState>>()
        val testObject = TestViewModel(this)

        testObject.combinedState.observeForever(observer)

        verify(observer).onChanged(TestState())
    }

    @Test
    fun `update state updates state`() = test {
        val initialState = TestState(someString = "stringA", showError = false, loading = false)
        val secondState = TestState(someString = "stringB", showError = true, loading = false)
        val thirdState = TestState(someString = "stringC", showError = false, loading = true)
        val observer = mock<Observer<in TestState>>()
        val testObject = object : BaseViewModel<TestState>(Store(initialState, this)) {
            override val errors: (Throwable) -> (TestState) -> TestState = {t: Throwable -> {state -> initialState}}
        }

        testObject.combinedState.observeForever(observer)

        testObject.action(ActionB)

        assertThat(testObject.combinedState.value, equalTo(secondState))

        testObject.action(ActionC)

        assertThat(testObject.combinedState.value, equalTo(thirdState))
    }


    @Test
    fun `force state immutability`() = test {
        var pass = false
        Thread.setDefaultUncaughtExceptionHandler { _, exception ->
            if(exception is IllegalStateException) pass = true
        }

        val testObject = TestViewModel(this)

        testObject.combinedState.observeForever(mock())

        testObject.action(ActionD)

        assertThat(pass, equalTo(true))

    }

    @Test
    fun `do not update if value old and new values are the same`() = test {
        val observer = mock<Observer<in String>>()
        val testObject = TestViewModel(this)

        testObject.someString.value.observeForever(observer)
        testObject.action(ActionC)

        verify(observer).onChanged("stringC")

        reset(observer)

        testObject.action(ActionC)

        verify(observer, never()).onChanged("stringC")
    }

    private data class TestState(val someString: String = "", val showError: Boolean = false, val loading: Boolean = false)

    private object ActionB : SyncStoreAction<TestState> {
        override fun fire() = {state: TestState -> state.copy(someString = "stringB", loading = false, showError = true) }

    }

    private object ActionC : AsyncStoreAction<TestState> {
        override suspend fun fire(currentState: TestState) = {state: TestState -> state.copy(someString = "stringC", loading = true, showError = false) }

    }

    private object ActionD : AsyncStoreAction<TestState> {
        override suspend fun fire(currentState: TestState) = {state: TestState -> state }

    }

    private class TestViewModel(dispatcher: CoroutineDispatcher) : BaseViewModel<TestState>(Store(TestState(), dispatcher)) {
        override val errors: (Throwable) -> (TestState) -> TestState = {t: Throwable -> {
                state -> TestState()
        }}
        val someString = observe {it.someString}
    }
}