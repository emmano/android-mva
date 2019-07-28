package me.emmano.androidmva.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class BindableViewHolder<M>(open val binding: ViewDataBinding, private val modelVariableId: Int)
    : RecyclerView.ViewHolder(binding.root) {

    open infix fun bind(model: M){
        binding.setVariable(modelVariableId, model)
        binding.executePendingBindings()
    }

}
