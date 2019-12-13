package me.emmano.androidmva.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BindableViewHolder<B:ViewDataBinding, M>(
    open val binding: B,
    private val modelVariableId: Int,
    private val dsl: DSL<B, M>?) : RecyclerView.ViewHolder(binding.root) {

    open infix fun bind(model: M) {
        binding.setVariable(modelVariableId, model)
        dsl?.let {
            dsl.bind?.let{
                it(binding, model)
            }
            dsl.onClick?.let { dsl -> binding.root.setOnClickListener {dsl(model)} }
        }

        binding.executePendingBindings()
    }

    companion object {
        fun <B:ViewDataBinding, M> create(
            parent: ViewGroup,
            modelVariableId: Int, @LayoutRes layoutId: Int,
            dsl: DSL<B, M>?)
                = DataBindingUtil.inflate<B>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false)
            .run { BindableViewHolder(this, modelVariableId, dsl) }
    }
}
