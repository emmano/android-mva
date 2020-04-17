package me.emmano.state.comics

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.ios.Ios

actual val engine: HttpClientEngine by lazy { Ios.create() }