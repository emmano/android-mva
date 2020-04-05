package me.emmano.state

interface Errors<S> {

    val errors: (Throwable)-> (S)->S
}