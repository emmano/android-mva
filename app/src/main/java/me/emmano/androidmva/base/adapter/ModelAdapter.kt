package me.emmano.androidmva.base.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ModelAdapter<M : Identity<M>>(
    private val dsl: AdapterDSL<M>,
    differ: DiffUtil.ItemCallback<M> = Differ()
) : ListAdapter<M, BindableViewHolder<*, M, *>>(differ) {


    private fun model(position: Int): M = getItem(position) as M

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolder<*, M, *> =
        dsl.create(parent, viewType)

    override fun getItemViewType(position: Int) = dsl.viewTypes(model(position))

    override fun onBindViewHolder(holder: BindableViewHolder<*, M, *>, position: Int) =
        holder bind model(position)

    private class Differ<M : Identity<M>> : DiffUtil.ItemCallback<M>() {
        override fun areItemsTheSame(oldItem: M, newItem: M) = oldItem.isSame(newItem)

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: M, newItem: M) = oldItem == newItem
    }
}

class AdapterDSL<M : Identity<M>> {

    lateinit var create: (ViewGroup, Int) -> BindableViewHolder<*, M, *>
    lateinit var viewTypes: (M) -> Int

    fun onCreateViewHolder(create: (ViewGroup, Int) -> BindableViewHolder<*, M, *>) {
        this.create = create
    }

    fun getViewTypes(viewTypes: (M) -> Int) {
        this.viewTypes = viewTypes
    }

    fun <B : ViewDataBinding, T : Identity<T>> holder(
        parent: ViewGroup,
        layoutId: Int,
        modelId: Int,
        dsl: (ViewHolderDSL<B, T>.() -> Unit)? = null
    ) = BindableViewHolder.create<B, M, T>(
        parent,
        modelId,
        layoutId,
        ViewHolderDSL<B, T>().apply {
            dsl?.let { dsl(this) }
        })

}

fun <M : Identity<M>> adapter(dsl: AdapterDSL<M>.() -> Unit) =
    ModelAdapter(AdapterDSL<M>().apply { dsl() })



