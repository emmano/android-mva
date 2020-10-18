package me.emmano.state

import kotlinx.coroutines.launch

abstract class BaseViewModel<S>(private val viewStateProvider: ViewStateProvider<S>) : ScopeProvider(), Errors<S> {

    val combinedState by lazy {
        viewStateProvider.error(errors)
        viewStateProvider.combinedState.wrap()
    }

    fun action(action: Action<S>) {
        scope.launch {
            viewStateProvider.dispatch(action)
        }
    }


}