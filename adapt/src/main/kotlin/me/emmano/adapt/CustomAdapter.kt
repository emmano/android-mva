package me.emmano.adapt

import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter : ListAdapter<Model, CustomViewHolder>(Differ()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CustomViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.header,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val header by lazy { view.findViewById<TextView>(R.id.header) }

    fun bind(model: Model) {
        header.text = (model as Header).title
    }

}

class Differ : DiffUtil.ItemCallback<Model>() {

    override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean = false

    override fun areContentsTheSame(oldItem: Model, newItem: Model) = false
}