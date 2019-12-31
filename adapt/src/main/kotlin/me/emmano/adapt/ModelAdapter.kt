package me.emmano.adapt

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ModelAdapter<M : Identity<M>>(
    private val dsl: AdapterDSL<M>,
    builder: AsyncDifferConfig.Builder<M> = AsyncDifferConfig.Builder(Differ())
) : ListAdapter<M, BindableViewHolder<*, M, *>>(builder.build()) {

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