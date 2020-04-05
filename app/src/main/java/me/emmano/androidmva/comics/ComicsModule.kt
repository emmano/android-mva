package me.emmano.androidmva.comics

import kotlinx.coroutines.Dispatchers
import me.emmano.androidmva.comics.api.MarvelService
import me.emmano.androidmva.comics.mvvm.ComicsViewModel
import me.emmano.androidmva.comics.repo.ComicRepository
import me.emmano.state.ViewStateProvider
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val comicsModule = module {
    single { get<Retrofit>().create(MarvelService::class.java) }
    single { ComicRepository(get()) }
    factory { ViewStateProvider(ComicsViewModel.State(), Dispatchers.IO) }
    viewModel { ComicsViewModel(get()) }
}