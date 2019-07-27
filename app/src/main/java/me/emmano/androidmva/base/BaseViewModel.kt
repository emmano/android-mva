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

    fun updateState(action: (S) -> S) {
        stateMutableLiveData.postValue(action)
    }

    fun <T> observe(stateToValue: (S) -> T): LiveData<T> = stateLiveData.mapExclusive(stateToValue)

    private fun <T, R> LiveData<T>.mapExclusive(mapper: (T) -> R): LiveData<R> {
        val result = MediatorLiveData<R>()
        result.addSource(this) { value ->
            val mappedValue = mapper(value)
            if (mappedValue != result.value) result.value = mappedValue
        }
        return result
    }
}

private fun <S> MutableLiveData<(S) -> S>.scan(initialState: S): LiveData<S> {
    val mediatorLiveData: MediatorLiveData<S> = MediatorLiveData()
    mediatorLiveData.value = initialState
    mediatorLiveData.addSource(this) { action ->
        val oldState = mediatorLiveData.value!!
        val newState = action(oldState)
        if (oldState === newState) throw IllegalStateException("State should be immutable. Make sure you call copy()")
        mediatorLiveData.postValue(newState)
    }
    return mediatorLiveData
}
