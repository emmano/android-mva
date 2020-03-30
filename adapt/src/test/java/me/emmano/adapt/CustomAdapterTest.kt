package me.emmano.adapt

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import com.nhaarman.mockitokotlin2.*
import me.emmano.adapt.base.LooperMockRunner
import me.emmano.adapt.base.MyTest
import me.emmano.adapt.base.Patch
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(LooperMockRunner::class)
@Patch(AsyncListDiffer::class)
class CustomAdapterTest {

    @Test
    fun name() {

        MyTest()
        val context = mock<Context>()
        val parent = mock<ViewGroup>{on { this.context } doReturn context}
        whenever(LayoutInflater.from(context).inflate(any<Int>(), any(), any())) doReturn mock()

        val testObject = CustomAdapter()

        val holder = testObject.onCreateViewHolder(parent, 0)

    }
}