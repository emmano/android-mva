package me.emmano.androidmva.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel<S>(initialState: S) : ViewModel() {

    private val stateMutableLiveData = MutableLiveData<(S) -> S>()

    @VisibleForTesting
    val stateLiveData: LiveData<S> = stateMutableLiveData.scan(initialState)

    fun updateState(reducer: (S) -> S) {
        stateMutableLiveData.postValue(reducer)
    }

    fun <T> observe(stateToValue: (S) -> T): Lazy<LiveData<T>> = stateLiveData.mapExclusive(stateToValue)

    private fun <T, R> LiveData<T>.mapExclusive(mapper: (T) -> R): Lazy<LiveData<R>> {
        val result = MediatorLiveData<R>()
        result.addSource(this) { value ->
            val mappedValue = mapper(value)
            if (mappedValue != result.value) result.value = mappedValue
        }
        return lazy { result }
    }
}

private fun <S> MutableLiveData<(S) -> S>.scan(initialState: S): LiveData<S> {
    val mediatorLiveData: MediatorLiveData<S> = MediatorLiveData()
    mediatorLiveData.value = initialState
    mediatorLiveData.addSource(this) { reduce ->
        val oldState = mediatorLiveData.value!!
        val newState = reduce(oldState)
        check(!(oldState === newState)) { "State should be immutable. Make sure you call copy()" }
        mediatorLiveData.postValue(newState)
    }
    return mediatorLiveData
}
