package me.emmano.androidmva.comics.mvvm

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import me.emmano.androidmva.base.RobolectricTest
import org.junit.Before
import org.junit.Test
import org.koin.core.inject

class ComicsFragmentTest : RobolectricTest() {

    private val viewModel: ComicsViewModel by inject()

    @Before
    fun setUp() {
        declareViewModelMock<ComicsViewModel> { on { comics } doReturn MutableLiveData() }

        launchFragmentInContainer<ComicsFragment>()
    }

    @Test
    fun `onViewCreated loads comics`() {
        verify(viewModel).loadComics()
    }
}