package me.emmano.androidmva.base

import android.util.Log
import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class Store<S>(private val initialState: S, val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val asyncActions by lazy { ConflatedBroadcastChannel<AsyncStoreAction<S>>() }
    private val syncActions by lazy { ConflatedBroadcastChannel<SyncStoreAction<S>>() }
    private val streamActions by lazy { ConflatedBroadcastChannel<StreamStoreAction<S>>() }

    private val state by lazy { ConflatedBroadcastChannel(initialState) }

    private lateinit var errorAction: (cause: Throwable) -> ((S) -> S)

    private val operation: suspend ((S, (S) -> S) -> S) = { acc, value ->
        val newState = value(acc)
        check(!(acc === newState)) { "State should be immutable. Make sure you call copy()" }
        newState
    }

    fun error(action: (cause: Throwable) -> ((S) -> S)) {
        errorAction = action
        Log.e("XXXX", "S")
    }

    private val streamStoreAction by lazy {
        streamActions.asFlow().switchFlatMap { it.fire(state.value) }
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

        flowOf(syncState, asyncState, streamStoreAction)
            .flattenMerge()
            .scan(initialState, operation)
            .map { state.send(it); it }
            .flowOn(dispatcher)
    }

    suspend fun dispatch(action: StoreAction<S>) {
        when (action) {
            is AsyncStoreAction -> asyncActions.send(action)
            is SyncStoreAction -> syncActions.send(action)
        }
    }

    fun <T> Flow<T>.myFirst() = flow {
        collect {
            emit(this@myFirst.first())
        }
    }


    fun Flow<StreamStoreAction<S>>.switchFlatMap(transform: suspend (value: StreamStoreAction<S>) -> Flow<(S) -> S>): Flow<(S) -> S> =
        flow {
            val typeToAction =
                mutableMapOf<Class<StreamStoreAction<S>>, BroadcastChannel<StreamStoreAction<S>>>()
            collect { action ->
                if (typeToAction.containsKey(action.javaClass)) {
                    typeToAction[action.javaClass]!!.send(action)
                } else {
                    val newChannel = BroadcastChannel<StreamStoreAction<S>>(1)
                    newChannel.asFlow().distinctUntilChanged().flatMapLatest { transform(it) }.collect {emit(it)}
                    newChannel.send(action)
                    typeToAction[action.javaClass] = newChannel
                }

            }
        }

}