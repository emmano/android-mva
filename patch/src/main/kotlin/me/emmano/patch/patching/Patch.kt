package me.emmano.patch.patching

import kotlin.reflect.KClass


@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.CLASS
)
annotation class Patch(
    val add: KClass<*>
)