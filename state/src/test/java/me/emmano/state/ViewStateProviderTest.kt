package me.emmano.state

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Rule
import org.junit.Test

class ViewStateProviderTest : CoroutineTest{

    @get:Rule
    override val coroutineRule = CoroutineTestRule()

    @get:Rule
    val rule = InstantTaskExecutorRule()


    @Test
    fun `sync action`() = test {
        val testObject = ViewStateProvider(TestState(), this)

        val state = testObject.combinedState.test()

        testObject.dispatch(Load)

        testObject.dispatch(UpdateName)

        state.assertEmitted(TestState(), TestState(true), TestState(true, "B"))
    }

    @Test
    fun `sync and async actions`() = test {
        val testObject = ViewStateProvider(TestState(), this)

        val state = testObject.combinedState.test()

        testObject.dispatch(Load)
        testObject.dispatch(LoadName(this))
        testObject.dispatch(UpdateName)

        state.assertValueAt(2, TestState(true, "B"))

        advanceTimeBy(100)

        state.assertValueAt(3, TestState(false, "A"))
    }

}

object Load : SyncStoreAction<TestState> {
    override fun fire() = {state: TestState -> state.copy(loading = true)}
}

object UpdateName : SyncStoreAction<TestState> {
    override fun fire() = {state: TestState -> state.copy(name = "B")}
}

class LoadName(val testCoroutineDispatcher: TestCoroutineDispatcher) : AsyncStoreAction<TestState> {
    override suspend fun fire(currentState: TestState): (TestState) -> TestState {
        testCoroutineDispatcher.delay(100)
        return {state: TestState -> state.copy(loading = false, name = "A")}
    }
}

data class TestState(val loading: Boolean = false, val name: String = "") {

}
