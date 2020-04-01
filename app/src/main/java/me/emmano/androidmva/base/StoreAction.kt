package me.emmano.androidmva.base

import kotlinx.coroutines.flow.Flow

interface StoreAction<S>

interface SyncStoreAction<S> : StoreAction<S>{
    fun fire() : (S) -> S
}

interface AsyncStoreAction<S> : StoreAction<S> {
    suspend fun fire(currentState: S) : (S) -> S
}

interface StreamStoreAction<S> : StoreAction<S> {
    suspend fun fire(currentState: S) : Flow<(S) -> S>
}