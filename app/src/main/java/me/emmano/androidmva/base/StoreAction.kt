package me.emmano.androidmva.base

interface StoreAction<S>

interface SyncStoreAction<S> : StoreAction<S>{
    fun fire() : (S) -> S
}

interface AsyncStoreAction<S> : StoreAction<S> {
    suspend fun fire(currentState: S) : (S) -> S
}