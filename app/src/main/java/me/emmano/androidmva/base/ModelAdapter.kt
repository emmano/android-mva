package me.emmano.androidmva.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class ModelAdapter<B: ViewDataBinding, M : Identity<M>>(
    private val modelId: Int,
    private val layoutId: Int,
    private val dsl: DSL<B, M>?,
    differ: DiffUtil.ItemCallback<M> = Differ()) : ListAdapter<M, BindableViewHolder<B,M>>(differ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BindableViewHolder.create(parent, modelId, layoutId, dsl)

    private fun model(position: Int): M = getItem(position) as M

    override fun onBindViewHolder(holder: BindableViewHolder<B,M>, position: Int) = holder bind model(position)

    private class Differ<M : Identity<M>> : DiffUtil.ItemCallback<M>() {
        override fun areItemsTheSame(oldItem: M, newItem: M) = oldItem.isSame(newItem)

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: M, newItem: M) = oldItem == newItem
    }
}

fun <B: ViewDataBinding, M : Identity<M>> adapter(
    modelId: Int,
    @LayoutRes layoutId: Int,
    dsl: (()->DSL<B,M>)? = null) : ModelAdapter<B, M> = object : ModelAdapter<B,M>(modelId, layoutId, dsl?.invoke()) {}

fun <M : Identity<M>> adapter(
    modelId: Int,
    @LayoutRes layoutId: Int)  : Lazy<ModelAdapter<ViewDataBinding, M>> =
    lazy { object : ModelAdapter<ViewDataBinding,M>(modelId, layoutId, null) {}}

open class DSL<B: ViewDataBinding,M>(val bind: ((B, M)->Unit)?, val onClick: ((M)->Unit)?)

fun <B: ViewDataBinding, M>bind(block: (B, M)->Unit) = object : DSL<B, M>(block, null){}
fun <B: ViewDataBinding, M>onClick(block: (M)->Unit) = object : DSL<B, M>(null, block){}