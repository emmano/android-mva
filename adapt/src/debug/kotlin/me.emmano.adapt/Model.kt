package me.emmano.adapt

sealed class Model : Identity<Model> {
    override fun isSame(other: Model) = false
}

data class Header(val title: String): Model()
data class Content(val content: String): Model()