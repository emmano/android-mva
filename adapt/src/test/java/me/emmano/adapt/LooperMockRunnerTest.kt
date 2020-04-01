package me.emmano.adapt

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.nhaarman.mockitokotlin2.mockingDetails
import me.emmano.adapt.base.LooperMockRunner
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(LooperMockRunner::class)
class LooperMockRunnerTest {

    @Test
    fun `getMainLooper returns mock`() {
        val looper = Looper.getMainLooper()
        val isMock = mockingDetails(looper).isMock
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