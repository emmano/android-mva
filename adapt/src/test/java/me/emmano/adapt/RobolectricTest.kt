package me.emmano.adapt

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.validateMockitoUsage
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.P], manifest= Config.NONE)
@RunWith(AndroidJUnit4::class)
abstract class RobolectricTest {

    @After
    fun validate() {
        // Avoid misleading messages from Mockito when mocks are misused.
        validateMockitoUsage()

    }

}