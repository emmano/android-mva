package me.emmano.androidmva.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class ModelAdapter<M>(differ: DiffUtil.ItemCallback<M> = Differ()) : ListAdapter<M, BindableViewHolder<M>>(differ) {

    override fun onBindViewHolder(holder: BindableViewHolder<M>, position: Int) = holder bind model(position)

    private fun model(position: Int) = getItem(position) as M

}