package me.emmano.androidmva.base

interface Errors<S> {

    val errors: (Throwable)-> (S)->S
}