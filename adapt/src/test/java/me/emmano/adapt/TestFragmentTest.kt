package me.emmano.adapt

import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.robolectric.shadows.ShadowToast

class TestFragmentTest: RobolectricTest() {

        @Test
        fun `test adapter dsl`() {
            val scenario = launchFragmentInContainer<TestFragment>()
            scenario.onFragment {
                val recyclerView = it.requireView().findViewById<RecyclerView>(R.id.recycler)

                val headerViewHolder =
                    recyclerView.findViewHolderForLayoutPosition(0) ?: throw IllegalStateException()

                val header =
                    headerViewHolder.itemView.findViewById<TextView>(R.id.header)

                assertThat(header.text.toString(), equalTo("Title"))

                header.performClick()

                val titleText = ShadowToast.getTextOfLatestToast()

                assertThat(titleText, equalTo("Title"))

                val contentViewHolder =
                    recyclerView.findViewHolderForLayoutPosition(1) ?: throw IllegalStateException()

                contentViewHolder.itemView.performClick()

                val rowText = ShadowToast.getTextOfLatestToast()

                assertThat(rowText, equalTo("Row Tapped"))

                val content =
                    contentViewHolder.itemView.findViewById<TextView>(R.id.content)

                assertThat(content.text.toString(), equalTo("Content"))

            }

        }
    }
