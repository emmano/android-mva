package me.emmano.androidmva.base

import androidx.lifecycle.*
import kotlinx.coroutines.*

 abstract class BaseViewModel<S>(private val store: Store<S>) : ViewModel(), Errors<S> {

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