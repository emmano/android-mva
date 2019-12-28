package me.emmano.adapt

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BindableViewHolder<B:ViewDataBinding, M : Identity<M>, T : Identity<T>>(
    private val binding: B,
    private val modelVariableId: Int,
    private val dsl: ViewHolderDSL<B, T>?) : RecyclerView.ViewHolder(binding.root) {

     infix fun bind(model: M) {
        binding.setVariable(modelVariableId, model)

        dsl?.let { dsl ->
            dsl.bind?.let{
                it(binding, cast(model))
            }
            dsl.onClick?.let { click -> binding.root.setOnClickListener {click(cast(model))} }
        }


        binding.executePendingBindings()
    }

    private fun cast(model: M) : T = model as T

    companion object {
        fun <B:ViewDataBinding, M: Identity<M>, T : Identity<T>> create(
            parent: ViewGroup,
            modelVariableId: Int, @LayoutRes layoutId: Int,
            dsl: ViewHolderDSL<B, T>?)
                = DataBindingUtil.inflate<B>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false)
            .run {
                BindableViewHolder<B, M, T>(
                    this,
                    modelVariableId,
                    dsl
                )
            }
    }
}
