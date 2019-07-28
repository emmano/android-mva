package me.emmano.androidmva.base

import android.os.Build.VERSION_CODES.P
import androidx.lifecycle.ViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.KStubbing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.validateMockitoUsage
import org.junit.After
import org.junit.runner.RunWith
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.robolectric.annotation.Config

@Config(sdk = [P], application = TestApplication::class, manifest=Config.NONE)
@RunWith(AndroidJUnit4::class)
abstract class RobolectricTest : AutoCloseKoinTest() {

    init { startKoin {} }

    @After
    fun validate() {
        // Avoid misleading messages from Mockito when mocks are misused.
        validateMockitoUsage()

    }

    protected inline fun <reified V : ViewModel> declareViewModelMock(mock: V = mock(), crossinline stubbing: KStubbing<V>.(V) -> Unit = {KStubbing(mock)}) =
         loadKoinModules(module{
             viewModel { KStubbing(mock).stubbing(mock);mock }
         })

}
