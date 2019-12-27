package me.emmano.androidmva.base.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ModelAdapter2<M:Identity<M>>(
    private val modelId: Int,
    private val dsl: DSL2<M>,
    differ: DiffUtil.ItemCallback<M> = Differ()
) : ListAdapter<M, BindableViewHolder<*, M>>(differ) {


    private fun model(position: Int): M = getItem(position) as M

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder<*, M>  = dsl.create(parent, viewType)

    override fun getItemViewType(position: Int) = dsl.viewTypes(model(position))

    override fun onBindViewHolder(holder: BindableViewHolder<*, M>, position: Int) = holder bind model(position)

    private class Differ<M : Identity<M>> : DiffUtil.ItemCallback<M>() {
        override fun areItemsTheSame(oldItem: M, newItem: M) = oldItem.isSame(newItem)

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: M, newItem: M) = oldItem == newItem
    }
}

class DSL2<M> {

    lateinit var create: (ViewGroup, Int) -> BindableViewHolder<*, M>
    lateinit var viewTypes: (M) -> Int

    fun onCreateViewHolder(create: (ViewGroup, Int) -> BindableViewHolder<*,M>) {
        this.create = create
    }

    fun getViewTypes(viewTypes: (M) -> Int) {
        this.viewTypes = viewTypes
    }

    fun <B: ViewDataBinding> holder(parent: ViewGroup, layoutId: Int, modelId: Int) = BindableViewHolder.create<B, M>(parent, modelId, layoutId, null)



}

fun <M: Identity<M>>adapter2(modelId: Int, dsl: DSL2<M>.() -> Unit) =
     ModelAdapter2(modelId, DSL2<M>().apply { dsl() })



