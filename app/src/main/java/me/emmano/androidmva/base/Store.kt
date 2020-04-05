package me.emmano.androidmva.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class Store<S>(private val initialState: S, val dispatcher: CoroutineDispatcher) {

    private val asyncActions by lazy { ConflatedBroadcastChannel<AsyncStoreAction<S>>() }
    private val syncActions by lazy { ConflatedBroadcastChannel<SyncStoreAction<S>>() }

    private val state by lazy { ConflatedBroadcastChannel(initialState) }

    private lateinit var errorAction: (cause: Throwable) -> ((S) -> S)

    private val operation: suspend ((S, (S) -> S) -> S) = { acc, value ->
        val newState = value(acc)
        check((acc !== newState)) { "State should be immutable. Make sure you call copy()" }
        newState
    }

    fun error(action: (cause: Throwable) -> ((S) -> S)) {
        errorAction = action
    }

    private val asyncState by lazy {
        asyncActions
            .asFlow()
            .flatMapConcat {
                flow { emit(it.fire(state.value)) }
                    .catch { emit(errorAction(it)) }
            }
    }

    private val syncState by lazy {
        syncActions
            .asFlow()
            .map { it.fire() }
    }

    val combinedState by lazy {

        flowOf(syncState, asyncState)
            .flattenMerge()
            .scan(initialState, operation)
            .map { state.send(it);
                it }
            .flowOn(dispatcher)
    }

    suspend fun dispatch(action: StoreAction<S>) {
        when (action) {
            is AsyncStoreAction -> asyncActions.send(action)
            is SyncStoreAction -> syncActions.send(action)
        }
    }

}