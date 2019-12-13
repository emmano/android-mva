package me.emmano.androidmva.base.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class ModelAdapter<B: ViewDataBinding, M : Identity<M>>(
    private val modelId: Int,
    private val layoutId: Int,
    private val dsl: DSL<B, M>?,
    differ: DiffUtil.ItemCallback<M> = Differ()
) : ListAdapter<M, BindableViewHolder<B, M>>(differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BindableViewHolder.create(
            parent,
            modelId,
            layoutId,
            dsl
        )

    private fun model(position: Int): M = getItem(position) as M

    override fun onBindViewHolder(holder: BindableViewHolder<B, M>, position: Int) = holder bind model(position)

    private class Differ<M : Identity<M>> : DiffUtil.ItemCallback<M>() {
        override fun areItemsTheSame(oldItem: M, newItem: M) = oldItem.isSame(newItem)

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: M, newItem: M) = oldItem == newItem
    }
}