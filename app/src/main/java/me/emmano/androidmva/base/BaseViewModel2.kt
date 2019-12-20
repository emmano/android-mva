package me.emmano.androidmva.base

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import me.emmano.androidmva.comics.mvvm.Store
import me.emmano.androidmva.comics.mvvm.StoreAction

open class BaseViewModel2<S, A: StoreAction>(private val initialState: S, private val store: Store<S, A>) : ViewModel() {

    private val stateMutableLiveData = MutableLiveData<A>()

    @VisibleForTesting
    val stateLiveData: LiveData<S>  =  stateMutableLiveData.scanAction(store)

    fun action(action: A) {
        stateMutableLiveData.postValue(action)
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

    private fun MutableLiveData<A>.scanAction(store: Store<S, A>): LiveData<S> {
        val mediatorLiveData: MediatorLiveData<S> = MediatorLiveData()
        mediatorLiveData.addSource(this) { action ->
            val oldState = mediatorLiveData.value ?: initialState
            viewModelScope.launch(store.dispatcher) {
                val newState = store.reduce(action, oldState)
                check(!(oldState === newState)) { "State should be immutable. Make sure you call copy()" }
                mediatorLiveData.postValue(newState)
            }
        }
        return mediatorLiveData
    }
}


