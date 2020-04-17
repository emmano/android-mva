package me.emmano.state

import kotlinx.coroutines.launch

abstract class BaseViewModel<S>(private val viewStateProvider: ViewStateProvider<S>) : ScopeProvider(), Errors<S> {

    val combinedState by lazy {
        viewStateProvider.error(errors)
        viewStateProvider.combinedState
    }

    fun action(action: StoreAction<S>) {
        scope.launch {
            viewStateProvider.dispatch(action)
        }
    }


}