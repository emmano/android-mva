package me.emmano.state.comics

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

actual val engine: HttpClientEngine by lazy { Android.create() }