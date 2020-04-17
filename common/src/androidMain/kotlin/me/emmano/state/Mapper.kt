package me.emmano.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.asLiveData

abstract class Mapper<S>(private val viewModel: BaseViewModel<S>) {

    fun <T> observe(stateToValue: (S) -> T) = viewModel.combinedState.asLiveData().mapExclusive(stateToValue)

    private fun <T, R> LiveData<T>.mapExclusive(mapper: (T) -> R): Lazy<LiveData<R>> {
        val result = MediatorLiveData<R>()
        result.addSource(this) { value ->
            val mappedValue = mapper(value)
            if (mappedValue != result.value) result.value = mappedValue
        }
        return lazy { result }
    }
}