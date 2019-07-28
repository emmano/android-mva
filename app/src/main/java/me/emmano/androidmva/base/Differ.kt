package me.emmano.androidmva.base

import androidx.recyclerview.widget.DiffUtil

class Differ<M> : DiffUtil.ItemCallback<M>() {
    override fun areItemsTheSame(oldItem: M, newItem: M) = oldItem === newItem

    override fun areContentsTheSame(oldItem: M, newItem: M) = oldItem == newItem
}
