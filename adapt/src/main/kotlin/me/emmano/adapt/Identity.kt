package me.emmano.adapt

interface Identity<in M> {
    fun isSame(other: M): Boolean
}