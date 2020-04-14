# Have you ever encountered code like the following and wondered how to test it?
```java
public class LooperDependent {
    static final Handler HANDLER =
    new Handler(Looper.getMainLooper()) {
      @Override
      public void handleMessage(Message msg) {
        throw new AssertionError("Unknown handler message received: " + msg.what);
      }
    };
    public void someMethodToVerify() {}
}
```

Probably you used Robolectric. 

It definitely feels like an overkill to have to use Robolectric just to avoid the the dreaded "Looper has not been mocked" `RuntimeException`, especially if `someMethodToVerify()` has no `Looper` dependent logic.

Here is where Patch comes in. Patch is a small bytecode instrumentor that returns mocks for certain Android classes so we can still write "pure" JUnit tests without having to rely on Robolectric.

# Usage
1. Just annotate the class under tests with `@RunWith(PatchingRunner::class)`. 
2. If needed, use `@Patch` to add a class that has a Looper dependency but comes from a third party library.
3. Add `testImplementation me.emmano:patch:x.y.z` to your app's module `build.gradle`.

Here is an example:

```kotlin
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
```

`@Patch` is used here because `LooperDependent`simulates an external dependency outside of our own package. Classes inside your app's package, Android classes, mockito and mockito-kotlin classes are picked up by default.

Here is another example. This shows how to write a `ListAdapter` test without Robolectric:

```kotlin
package me.emmano.adapt

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import me.emmano.patch.PatchingRunner
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(PatchingRunner::class)
class ModelAdapterTest  {

    @Test
    fun `onCreateViewHolder - delegates to dsl for view holder creation`() {
        val bindableViewHolder : BindableViewHolder<*, Model, *> = BindableViewHolder<ViewDataBinding, Model, Model>(
            mock{ on { root } doReturn mock()}, 0, mock()
        )
        val viewHolder : (ViewGroup, Int) -> BindableViewHolder<*, Model, *> =  { _, _ ->
            bindableViewHolder
        }
        val adapterDSL = AdapterDSL<Model>().apply { onCreateViewHolder(viewHolder) }
        val testObject = ModelAdapter(adapterDSL)

        val viewType = 0
        val parent = mock<ViewGroup>()

        val holder = testObject.onCreateViewHolder(parent, viewType)

        assertThat(holder, equalTo(bindableViewHolder))
    }

    @Test
    fun `getItemViewType - delegates to dsl for view types`() {

        val model = mock<Model>()
        val viewType = 1
        val viewTypes: (Model) -> Int = {viewType}
        val adapterDSL = AdapterDSL<Model>().apply { getViewTypes(viewTypes) }

        val testObject = ModelAdapter(adapterDSL)
        testObject.submitList(listOf(model))

        val position = 0

        val actualViewType = testObject.getItemViewType(position)

        assertThat(actualViewType, equalTo(viewType))

    }

    @Test
    fun `onBindViewHolder - delegates to dsl for view types`() {

        val model = mock<Model>()
        val adapterDSL: AdapterDSL<Model> = mock()

        val testObject = ModelAdapter(adapterDSL)

        testObject.submitList(listOf(model))

        val position = 0

        val holder = mock<BindableViewHolder<*, Model, *>>()

        testObject.onBindViewHolder(holder, position)

        verify(holder) bind model
    }
}
```

# Benefits:
1. Tests run faster when compared to using Robolectric. Patcher loads and instruments a much smaller subset of Android classes.
2. Patcher does not have to download `android.jar`.
3. It sheds some light on how Robolectric works since the mechanism for loading and instrumenting classes is the same as Robolectric's.

Here is a side by side comparison for the test shown above. Robolectric was already downloaded before running the test. Having to download robolectric added around half a second more to the test run.

![Tests ran using Patcher](https://imgur.com/b0jT0MG)

![Tests ran using Robolectric](https://imgur.com/YJT6U6a)

# TODO
1. Add support for `android.util.Log`


**Note:** By no means Patcher is a replacement for Robolectric or serves the same purpose. Patcher is just a bytecode instrumentor.
