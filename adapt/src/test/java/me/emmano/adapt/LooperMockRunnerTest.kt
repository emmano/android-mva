package me.emmano.adapt

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.nhaarman.mockitokotlin2.mockingDetails
import com.nhaarman.mockitokotlin2.spy
import me.emmano.adapt.base.LooperMockRunner
import me.emmano.adapt.base.Patch
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(LooperMockRunner::class)
@Patch(LooperUser::class)
class LooperMockRunnerTest {

    @Test
    fun `getMainLooper returns mock`() {
        val looper = spy(Looper.getMainLooper())
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