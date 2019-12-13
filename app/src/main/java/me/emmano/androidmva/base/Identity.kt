package me.emmano.androidmva.base

interface Identity<M> {
    fun isSame(other: M): Boolean
}