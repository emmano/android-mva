package me.emmano.androidmva.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import java.lang.IllegalStateException


class BaseViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `initial state is broadcasted`() {
        val initialState = mock<TestState>()
        val observer = mock<Observer<in TestState>>()
        val testObject = BaseViewModel(initialState)

        testObject.stateLiveData.observeForever(observer)

        verify(observer).onChanged(initialState)
    }

    @Test
    fun `update state updates state`() {
        val initialState = TestState(someString = "stringA", showError = false, loading = false)
        val secondState = TestState(someString = "stringB", showError = true, loading = false)
        val thirdState = TestState(someString = "stringC", showError = false, loading = true)
        val observer = mock<Observer<in TestState>>()
        val testObject = BaseViewModel(initialState)

        testObject.stateLiveData.observeForever(observer)

        testObject.updateState {oldState -> oldState.copy(someString = "stringB", showError = true)}
        assertThat(testObject.stateLiveData.value, equalTo(secondState))

        testObject.updateState {oldState -> oldState.copy(someString = "stringC", showError = false, loading = true)}
        assertThat(testObject.stateLiveData.value, equalTo(thirdState))
    }

    @Test(expected = IllegalStateException::class)
    fun `force state immutability`() {
        val initialState = TestState(someString = "stringA", showError = false, loading = false)
        val testObject = BaseViewModel(initialState)

        testObject.stateLiveData.observeForever(mock())

        testObject.updateState { it }
    }

    @Test
    fun `do not update if value old and new values are the same`() {
        val observer = mock<Observer<in String>>()
        val testObject = TestViewModel()
        testObject.someString.value.observeForever(observer)

        testObject.updateState { it.copy(someString = "stringA") }

        verify(observer).onChanged("stringA")

        reset(observer)

        testObject.updateState { it.copy(someString = "stringA") }

        verify(observer, never()).onChanged("stringA")
    }

    private data class TestState(val someString: String = "", val showError: Boolean = false, val loading: Boolean = false)

    private class TestViewModel : BaseViewModel<BaseViewModelTest.TestState>(TestState()) {

        val someString = observe {it.someString}
    }
}