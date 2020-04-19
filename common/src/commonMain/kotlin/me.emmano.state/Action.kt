package me.emmano.state

interface Action<S>

interface SyncAction<S> : Action<S>{
    fun fire() : (S) -> S
}

interface AsyncAction<S> : Action<S> {
    suspend fun fire(currentState: S) : (S) -> S
}