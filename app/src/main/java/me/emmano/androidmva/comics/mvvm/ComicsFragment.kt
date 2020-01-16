











package me.emmano.androidmva.comics.mvvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import me.emmano.adapt.adapter
import me.emmano.androidmva.BR
import me.emmano.androidmva.R
import me.emmano.androidmva.databinding.FragmentComicsBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ComicsFragment : Fragment() {

    private val viewModel: ComicsViewModel by viewModel()
    private val adapter by adapter<ComicModel>(BR.comicModel, R.layout.comic_cell)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
           FragmentComicsBinding.inflate(inflater, container, false).apply{
               lifecycleOwner = this@ComicsFragment
               vm = viewModel
               comics.adapter = adapter
               comics.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
               refresh.setOnRefreshListener {
                   lifecycleScope.launch {
                       viewModel.loadComics()
                   }
               }
           }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadComics()
    }

    override fun onResume() {
        super.onResume()
        viewModel.comics.observe(this, Observer {
            adapter.submitList(it)
        })
    }

}