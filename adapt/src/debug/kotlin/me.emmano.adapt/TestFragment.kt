package me.emmano.adapt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import me.emmano.adapt.databinding.ContentBinding
import me.emmano.adapt.databinding.FragmentTestBinding
import me.emmano.adapt.databinding.HeaderBinding

class TestFragment : Fragment() {

    private val adapter by adapter<Model> {

        val TYPE_HEADER = 1
        val TYPE_CONTENT = 2

        onCreateViewHolder { parent, viewType ->
            when (viewType) {
                TYPE_HEADER -> holder<HeaderBinding, Header>(parent, R.layout.header, BR.header) {

                    onBind { binding, header ->
                        binding.header.setOnClickListener {
                            Toast.makeText(
                                this@TestFragment.requireContext(),
                                header.title,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                TYPE_CONTENT -> holder<ContentBinding, Content>(parent, R.layout.content, BR.content) {

                    onClick {
                        Toast.makeText(
                            this@TestFragment.requireContext(),
                            "Row Tapped",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    onBind { binding, content ->
                        binding.content.setOnClickListener {
                            Toast.makeText(
                                this@TestFragment.requireContext(),
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
    }.apply { value.submitList(listOf(Header("Title"), Content("Content"))) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTestBinding.inflate(inflater, container, false).apply {
        lifecycleOwner = viewLifecycleOwner
        recycler.adapter = adapter
    }.root

}

