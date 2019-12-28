package me.emmano.adapt

sealed class Model : Identity<Model> {
    override fun isSame(other: Model) = false
}

data class TestModel(val title: String): Model()