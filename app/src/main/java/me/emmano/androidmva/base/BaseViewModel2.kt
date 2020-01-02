package me.emmano.androidmva.base

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import me.emmano.androidmva.comics.mvvm.ComicsViewModel
import me.emmano.androidmva.comics.mvvm.LoadingComicsException
import me.emmano.androidmva.comics.repo.ComicRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseViewModel2<S>(val store: Store<S>) : ViewModel() {

    private lateinit var errorAction: suspend FlowCollector<S>.(cause: Throwable) -> Unit
    val combinedState by lazy {
        store.combinedState.asLiveData()
    }


    fun action(action: StoreAction<S>) {
        viewModelScope.launch(store.dispatcher) {
            store.dispatch(action)
        }
    }

    fun error(action: suspend (cause: Throwable) -> ((S) -> S)) {
        store.error(action)
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

class Store<S>(private val initialState: S, val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val asyncActions by lazy { ConflatedBroadcastChannel<AsyncStoreAction<S>>() }
    private val syncActions by lazy { ConflatedBroadcastChannel<SyncStoreAction<S>>() }

    private val state by lazy { ConflatedBroadcastChannel(initialState) }

    private lateinit var errorAction: suspend (cause: Throwable) -> ((S) -> S)


    val operation: suspend ((S, (S) -> S) -> S) = { acc, value ->
        value(acc)
    }

    fun error(action: suspend (cause: Throwable) -> ((S) -> S)) {
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
