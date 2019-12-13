package me.emmano.androidmva.base.adapter

interface Identity<M> {
    fun isSame(other: M): Boolean
}