package me.emmano.state

import kotlinx.coroutines.CoroutineScope

expect open class ScopeProvider() {
    val scope: CoroutineScope
    protected open fun onCleared()
}