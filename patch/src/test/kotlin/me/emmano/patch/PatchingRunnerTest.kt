package me.emmano.patch

import android.os.Handler
import android.os.Looper
import android.os.Message
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(PatchingRunner::class)
class PatchingRunnerTest {

    @Test
    fun `getMainLooper returns mock`() {
        val looper = Looper.getMainLooper()
        val isMock = Mockito.mockingDetails(looper).isMock
        assertThat(isMock, equalTo(true))
        LooperUser()
    }
}

class LooperUser {
    private val HANDLER: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
        }
    }
}