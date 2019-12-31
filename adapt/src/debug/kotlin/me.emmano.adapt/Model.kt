package me.emmano.adapt

sealed class Model : Identity<Model>

data class Header(val someUniqueId: Int = 0, val title: String): Model() {
    override fun isSame(other: Model) = someUniqueId == (other as Header).someUniqueId
}

data class Content(val someUniqueId: Int = 1, val content: String): Model() {
    override fun isSame(other: Model)=  someUniqueId == (other as Content).someUniqueId
}