package me.emmano.state

import kotlinx.coroutines.CoroutineDispatcher

expect object Dispatchers {
    val dispatcher: CoroutineDispatcher
}