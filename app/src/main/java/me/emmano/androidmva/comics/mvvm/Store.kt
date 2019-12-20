package me.emmano.androidmva.comics.mvvm

import kotlinx.coroutines.CoroutineDispatcher

interface Store<S, in A: StoreAction>{
    val dispatcher: CoroutineDispatcher
     suspend fun reduce(action: A, state: S): S
}