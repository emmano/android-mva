package me.emmano.androidmva.comics.mvvm

import androidx.lifecycle.viewModelScope
import com.dropbox.android.external.store4.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.emmano.androidmva.comics.mvvm.ComicsViewModel.State
import me.emmano.state.BaseViewModel
import me.emmano.state.ViewStateProvider

class ComicsViewModel(private val store: Store<String, List<ComicModel>>, viewStateProvider: ViewStateProvider<State>) :
    BaseViewModel<State>(viewStateProvider) {

    override val errors = { t: Throwable ->
        ShowError
    }

    val comics by observe {
        it.comics
    }
    val loading by observe {
        it.loading
    }

    fun loadComics(refresh: Boolean) {

        viewModelScope.launch {
            store.stream( StoreRequest.cached("load", refresh)).collect{ response: StoreResponse<List<ComicModel>> ->
                when(response) {
                    is StoreResponse.Loading -> action(Loading)
                    is StoreResponse.Data<List<ComicModel>> -> {
                        if (response.origin == ResponseOrigin.Fetcher)
                            action(LoadComics(response.value))
                    }
                    is StoreResponse.Error -> {
                        if (response.origin == ResponseOrigin.Fetcher)
                            action(Error)
                    }
                }
            }

        }
    }

    data class State(
        val comics: List<ComicModel> = emptyList(),
        val loading: Boolean = false,
        val showError: Boolean = false
    )

}

