package me.emmano.state

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<S>(private val viewStateProvider: ViewStateProvider<S>) : ViewModel(), Errors<S> {

    @VisibleForTesting
    val combinedState by lazy {
        viewStateProvider.error(errors)
        viewStateProvider.combinedState.asLiveData()
    }

    fun action(action: StoreAction<S>) {
        viewModelScope.launch(viewStateProvider.dispatcher) {
            viewStateProvider.dispatch(action)
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