package me.emmano.androidmva.comics.mvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import me.emmano.androidmva.BR
import me.emmano.androidmva.base.BindableViewHolder
import me.emmano.androidmva.base.ModelAdapter
import me.emmano.androidmva.databinding.ComicCellBinding

class ComicModelAdapter : ModelAdapter<ComicModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ComicModelViewHolder.create(parent)

    class ComicModelViewHolder(override val binding: ComicCellBinding) : BindableViewHolder<ComicModel>(binding, BR.comicModel) {
        companion object {
            fun create(parent: ViewGroup) =
                ComicModelViewHolder(
                    ComicCellBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        }
    }
}