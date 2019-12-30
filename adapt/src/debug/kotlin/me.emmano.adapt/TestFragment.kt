package me.emmano.adapt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.emmano.adapt.databinding.FragmentTestBinding

class TestFragment : Fragment() {

    private val adapter by adapter()
        .apply { value.submitList(listOf(Header(title = "Title"), Content(content = "Content"))) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTestBinding.inflate(inflater, container, false).apply {
        lifecycleOwner = viewLifecycleOwner
        recycler.adapter = adapter
    }.root

}

