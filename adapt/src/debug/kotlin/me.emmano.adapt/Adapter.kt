package me.emmano.adapt

import android.widget.Toast
import me.emmano.adapt.databinding.ContentBinding
import me.emmano.adapt.databinding.HeaderBinding

fun TestFragment.adapter() = adapter<Model> {

    val TYPE_HEADER = 1
    val TYPE_CONTENT = 2

    onCreateViewHolder { parent, viewType ->
        when (viewType) {
            TYPE_HEADER -> holder<HeaderBinding, Header>(parent, R.layout.header, BR.header) {

                onBind { binding, header ->
                    binding.header.setOnClickListener {
                        Toast.makeText(
                            this@adapter.requireContext(),
                            header.title,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            TYPE_CONTENT -> holder<ContentBinding, Content>(parent, R.layout.content, BR.content) {

                onClick {
                    Toast.makeText(
                        this@adapter.requireContext(),
                        "Row Tapped",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                onBind { binding, content ->
                    binding.content.setOnClickListener {
                        Toast.makeText(
                            this@adapter.requireContext(),
                            content.content,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

            else -> throw IllegalStateException("ViewHolder not supported for itemViewType: $viewType")
        }
    }

    getViewTypes {
        when (it) {
            is Header -> TYPE_HEADER
            is Content -> TYPE_CONTENT
        }
    }
}