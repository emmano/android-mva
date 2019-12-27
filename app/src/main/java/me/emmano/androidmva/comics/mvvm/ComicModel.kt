package me.emmano.androidmva.comics.mvvm

import me.emmano.androidmva.base.adapter.Identity


sealed class ComicModel :
    Identity<ComicModel> {
    override fun isSame(other: ComicModel) = false
}

data class ComicCell(val title: String, val description: String, val imageUrl: String) : ComicModel()
