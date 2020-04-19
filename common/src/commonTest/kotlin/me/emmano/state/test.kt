package me.emmano.state

import kotlinx.coroutines.CoroutineScope

expect fun <T> test(block: suspend (CoroutineScope) -> T)