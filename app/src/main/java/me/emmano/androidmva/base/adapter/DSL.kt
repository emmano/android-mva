package me.emmano.androidmva.base.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

class DSL<B: ViewDataBinding,M>(val bind: ((B, M)->Unit)?, val onClick: ((M)->Unit)?)

fun <B: ViewDataBinding, M : Identity<M>> adapter(
    modelId: Int,
    @LayoutRes layoutId: Int,
    dsl: (()-> DSL<B, M>)? = null) : ModelAdapter<B, M> = object : ModelAdapter<B, M>(modelId, layoutId, dsl?.invoke()) {}

fun <M : Identity<M>> adapter(
    modelId: Int,
    @LayoutRes layoutId: Int)  : Lazy<ModelAdapter<ViewDataBinding, M>> =
    lazy { object : ModelAdapter<ViewDataBinding, M>(modelId, layoutId, null) {}}

fun <B: ViewDataBinding, M>bind(block: (B, M)->Unit) =
    DSL(block, null)
fun <B: ViewDataBinding, M>onClick(block: (M)->Unit) =
    DSL<B, M>(null, block)