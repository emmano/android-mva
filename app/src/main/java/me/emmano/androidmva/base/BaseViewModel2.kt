package me.emmano.androidmva.base

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import me.emmano.androidmva.comics.mvvm.ComicsViewModel
import me.emmano.androidmva.comics.repo.ComicRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

open class BaseViewModel2<S>(initialState: S, val store: Store<S>) : ViewModel() {

    val combinedState by lazy { store.combinedState.asLiveData() }


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
    private val syncActions by lazy { ConflatedBroadcastChannel<SyncStoreAction<S>>() }

    private val state by lazy { ConflatedBroadcastChannel(initialState) }

    val operation: suspend ((S, (S) -> S) -> S) = { acc, value ->  value(acc) }

    private val asyncState = asyncActions.asFlow().flatMapConcat { flow{ emit(it.fire(state.value))} }

    private val syncState = syncActions.asFlow().map {
        Timber.e("ACTION: $it")
        it.fire()
    }

    val combinedState = flowOf(syncState, asyncState).flattenMerge().scan(initialState, operation).map {
        Timber.e("STATE: $it")
        state.send(it); it }

    suspend fun dispatch(action: StoreAction<S>) {
        Timber.e("DISPATCH($action): Thread ${Thread.currentThread().name}")
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

object LoadComics :
    AsyncStoreAction<ComicsViewModel.State>, KoinComponent {

    private val comicRepository by inject<ComicRepository>()

    override suspend fun fire(currentState: ComicsViewModel.State): (ComicsViewModel.State) -> ComicsViewModel.State {
        val comics = comicRepository.comics().orEmpty()
        return {state: ComicsViewModel.State -> state.copy(comics, false, false)}
    }
}

object ShowError : SyncStoreAction<ComicsViewModel.State> {
    override fun fire() = {state: ComicsViewModel.State -> state.copy(loading = false, showError = true) }


}