package me.emmano.androidmva.base

import androidx.lifecycle.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import me.emmano.androidmva.comics.mvvm.ComicsViewModel
import me.emmano.androidmva.comics.repo.ComicRepository
import timber.log.Timber

open class BaseViewModel2<S>(initialState: S, val store: Store<S>) : ViewModel() {

    val combinedState by lazy {
        flowOf(store.syncState, store.asyncState).flattenMerge().asLiveData()
    }


    fun action(action: StoreAction<S>) {
        Timber.e("HAS OBSERVERS: ${combinedState.hasActiveObservers()}")

        viewModelScope.launch {
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

class Store<S>(private val initialState: S) {

    private val asyncActions by lazy { ConflatedBroadcastChannel<AsyncStoreAction<S>>() }
    private val state by lazy { ConflatedBroadcastChannel(initialState) }

    val operation: suspend (S, StoreAction<S>) -> S = { value, acc ->
        val newState = acc.reduce(value)
        if (value != state.value) {
            state.send(acc.reduce(state.value))
        } else {
            state.send(newState)
        }
        state.value
    }

    val asyncState = asyncActions.asFlow()
        .scan(initialState, operation)

    suspend fun dispatch(action: StoreAction<S>) {
        Timber.e("DISPATCH($action): Thread ${Thread.currentThread().name}")
        when (action) {
            is AsyncStoreAction -> asyncActions.send(action)
            is SyncStoreAction -> syncActions.send(action)
        }

    }

    private val syncActions by lazy { ConflatedBroadcastChannel<SyncStoreAction<S>>() }


    val syncState = syncActions.asFlow()
        .scan(initialState, operation)


}

interface StoreAction<S> {
    suspend fun reduce(state: S): S
}

interface SyncStoreAction<S> : StoreAction<S>

interface AsyncStoreAction<S> : StoreAction<S>

object Loading : SyncStoreAction<ComicsViewModel.State> {

    override suspend fun reduce(state: ComicsViewModel.State): ComicsViewModel.State {
        Timber.e("LOADING: Thread ${Thread.currentThread().name}")
        delay(2000)
        return state.copy(loading = true, showError = false)
    }
}

class LoadComics(private val repository: ComicRepository) :
    AsyncStoreAction<ComicsViewModel.State> {

    override suspend fun reduce(state: ComicsViewModel.State): ComicsViewModel.State {

        val comics = repository.comics().orEmpty()
        Timber.e("LOADED COMICS: Thread ${Thread.currentThread().name} $comics")
        return state.copy(comics = comics, loading = false, showError = false)
    }
}

object ShowError : SyncStoreAction<ComicsViewModel.State> {

    override suspend fun reduce(state: ComicsViewModel.State): ComicsViewModel.State {
        return state.copy(loading = false, showError = true)
    }

}