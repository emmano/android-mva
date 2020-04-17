package me.emmano.androidmva.comics.mvvm

import me.emmano.adapt.Identity

data class AndroidComicModel(val title: String, val description: String, val imageUrl: String) : Identity<AndroidComicModel> {
    override fun isSame(other: AndroidComicModel) = title == other.title
}
