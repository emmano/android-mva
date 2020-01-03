package me.emmano.androidmva.base

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import me.emmano.androidmva.comics.mvvm.ComicsViewModel
import me.emmano.androidmva.comics.repo.ComicRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

 abstract class BaseViewModel2<S>(private val store: Store<S>) : ViewModel(), Errors<S> {

    val combinedState by lazy {
        store.error(errors)
        store.combinedState.asLiveData()
    }

    fun action(action: StoreAction<S>) {
        viewModelScope.launch(store.dispatcher) {
            store.dispatch(action)
        }
    }

    fun <T> observe(stateToValue: (S) -> T) = combinedState.mapExclusive(stateToValue)

    private fun <T, R> LiveData<T>.mapExclusive(mapper: (T) -> R): Lazy<LiveData<R>> {
        val result = MediatorLiveData<R>()
        result.addSource(this) { value ->
            val mappedValue = mapper(value)
            if (mappedValue != result.value) result.value = mappedValue
        }
        return lazy { result }
    }
}

interface Errors<S> {

    val errors: (Throwable)-> (S)->S
}

class Store<S>(private val initialState: S, val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val asyncActions by lazy { ConflatedBroadcastChannel<AsyncStoreAction<S>>() }
    private val syncActions by lazy { ConflatedBroadcastChannel<SyncStoreAction<S>>() }

    private val state by lazy { ConflatedBroadcastChannel(initialState) }

    private lateinit var errorAction:  (cause: Throwable) -> ((S) -> S)

    private val operation: suspend ((S, (S) -> S) -> S) = { acc, value ->
        val newState = value(acc)
        check(!(acc === newState)) { "State should be immutable. Make sure you call copy()" }
        newState
    }

    fun error(action:  (cause: Throwable) -> ((S) -> S)) {
        errorAction = action
    }

    private val asyncState by lazy {   asyncActions.asFlow()
        .flatMapConcat { flow{ emit(it.fire(state.value))}.catch {
            emit(errorAction(it))
        }
        }}

    private val syncState by lazy { syncActions.asFlow().map {
        it.fire()
    }}

    val combinedState by lazy { flowOf(syncState, asyncState).flattenMerge().scan(initialState, operation).map {
        state.send(it)
        ; it }.flowOn(dispatcher)}

    suspend fun dispatch(action: StoreAction<S>) {
        when (action) {
            is AsyncStoreAction -> asyncActions.send(action)
            is SyncStoreAction -> syncActions.send(action)
        }
    }

}

interface StoreAction<S>

interface SyncStoreAction<S> : StoreAction<S>{
     fun fire() : (S) -> S
}

interface AsyncStoreAction<S> : StoreAction<S> {
    suspend fun fire(currentState: S) : (S) -> S
}

object Loading : SyncStoreAction<ComicsViewModel.State> {
    override fun fire() = {state: ComicsViewModel.State -> state.copy(loading = true, showError = false) }

}

class LoadComics :
    AsyncStoreAction<ComicsViewModel.State>, KoinComponent {

    private val comicRepository by inject<ComicRepository>()

    override suspend fun fire(currentState: ComicsViewModel.State): (ComicsViewModel.State) -> ComicsViewModel.State {
        val comics = comicRepository.comics().orEmpty()
        return {state: ComicsViewModel.State -> state.copy(comics, false, false)}
    }
}

val ShowError  = {state: ComicsViewModel.State -> state.copy(loading = false, showError = true) }
