package me.emmano.androidmva.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.emmano.androidmva.comics.mvvm.ComicsViewModel
import me.emmano.androidmva.comics.repo.ComicRepository
import timber.log.Timber

open class BaseViewModel2<S>(initialState: S, val store: Store<S>) : ViewModel() {

    @VisibleForTesting
    val stateLiveData: LiveData<S> by lazy {
        store.flow
            .scan(initialState)
            { value, acc ->
               acc.reduce(value)
            }
            .flowOn(Dispatchers.IO)
            .asLiveData()

    }

    fun action(action: StoreAction<S>)  {
         viewModelScope.launch {
             store.dispatch(action)

         }
         }

    fun <T> observe(stateToValue: (S) -> T) = stateLiveData.mapExclusive(stateToValue)

    private fun <T, R> LiveData<T>.mapExclusive(mapper: (T) -> R): LiveData<R> {
        val result = MediatorLiveData<R>()
        result.addSource(this) { value ->
            val mappedValue = mapper(value)
            if (mappedValue != result.value) result.value = mappedValue
        }
        return result
    }
}

class Store<S> {

    val actions by lazy { ConflatedBroadcastChannel<StoreAction<S>>()}
    val flow = actions.asFlow()

      suspend fun dispatch(action: StoreAction<S>) {
         Timber.e("DISPATCH($action): Thread ${Thread.currentThread().name}")
              actions.send(action)
    }

}

interface StoreAction<S>{
    suspend fun reduce(state: S) : S
}

object Loading : StoreAction<ComicsViewModel.State> {

    override suspend fun reduce(state: ComicsViewModel.State): ComicsViewModel.State {
        Timber.e("LOADING: Thread ${Thread.currentThread().name}")
        return state.copy(loading = true, showError = false)
    }
}

class LoadComics(private val repository: ComicRepository) : StoreAction<ComicsViewModel.State> {

    override suspend fun reduce(state: ComicsViewModel.State): ComicsViewModel.State {

        val comics = repository.comics().orEmpty()
        Timber.e("LOADED COMICS: Thread ${Thread.currentThread().name} $comics")
        return state.copy(comics = comics, loading = false, showError = false)
    }
}

object ShowError : StoreAction<ComicsViewModel.State> {

    override suspend fun reduce(state: ComicsViewModel.State): ComicsViewModel.State {
        return state.copy(loading = false, showError = true)
    }

}