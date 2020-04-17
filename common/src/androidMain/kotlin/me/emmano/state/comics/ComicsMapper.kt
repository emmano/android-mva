package me.emmano.state.comics

import me.emmano.state.Mapper

class ComicsMapper(viewModel: ComicsViewModel) : Mapper<ComicsViewModel.State>(viewModel) {

    val comics by observe { it.comics }
    val loading by observe { it.loading }

}