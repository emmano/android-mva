package me.emmano.androidmva.base.adapter

interface Identity<in M> {
    fun isSame(other: M): Boolean
}