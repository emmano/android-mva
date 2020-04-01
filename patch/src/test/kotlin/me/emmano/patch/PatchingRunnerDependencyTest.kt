package me.emmano.patch

import android.os.Looper
import me.emmano.patch.patching.Patch
import some.external.dependency.LooperDependent
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(PatchingRunner::class)
@Patch(LooperDependent::class)
class PatchingRunnerDependencyTest {

    @Test
    fun `getMainLooper returns mock`() {
        val looper = Looper.getMainLooper()
        val isMock = Mockito.mockingDetails(looper).isMock
        assertThat(isMock, equalTo(true))
        LooperDependent()
    }
}