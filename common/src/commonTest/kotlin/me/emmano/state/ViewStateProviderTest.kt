package me.emmano.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlin.test.Test

open class ViewStateProviderTest {

    @Test
    fun `sync action`() = test {
        val testObject = ViewStateProvider(TestState())

        val state = testObject.combinedState.test(it)

        testObject.dispatch(Load)

        testObject.dispatch(UpdateName)

        state.assertEmitted(TestState(), TestState(true), TestState(true, "B"))
    }

    @Test
    fun `sync and async actions`() = test {
        val testObject = ViewStateProvider(TestState())

        val state = testObject.combinedState.test(it)

        testObject.dispatch(Load)
        testObject.dispatch(LoadName())
        testObject.dispatch(UpdateName)

        state.assertValueAt(2, TestState(true, "B"))
        delay(100)

        state.assertValueAt(3, TestState(false, "A"))
    }

}

object Load : SyncAction<TestState> {
    override fun fire() = {state: TestState -> state.copy(loading = true)}
}

object UpdateName : SyncAction<TestState> {
    override fun fire() = {state: TestState -> state.copy(name = "B")}
}

class LoadName() : AsyncAction<TestState> {
    override suspend fun fire(currentState: TestState): (TestState) -> TestState {
        delay(100)
        return {state: TestState -> state.copy(loading = false, name = "A")}
    }
}

data class TestState(val loading: Boolean = false, val name: String = "")

fun <T> Flow<T>.test(scope: CoroutineScope): TestFlow<T> = TestFlow(this, scope)

