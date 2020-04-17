package me.emmano.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import me.emmano.state.Dispatchers.dispatcher

actual open class ScopeProvider {

    private val viewModelJob = SupervisorJob()
    actual val scope: CoroutineScope = CoroutineScope(dispatcher + viewModelJob)

    protected actual open fun onCleared() {
        viewModelJob.cancelChildren()
    }
}