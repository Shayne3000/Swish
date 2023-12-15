package com.senijoshua.swish.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * Custom matchers for the RecyclerView list item views.
 */
object RecyclerViewItemMatcher {
    /**
     * Given a viewId, check that the supplied matcher matches the View (with said viewId)
     * in the root view of the ViewHolder at the given adapter position.
     */
    fun childViewOfItemAtPosition(adapterPosition: Int, childViewId: Int, childViewMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("Given a view id, check that the matcher matches " +
                        "a view in the viewholder at the given adapter position.")
                childViewMatcher.describeTo(description)
            }

            override fun matchesSafely(recyclerView: RecyclerView?): Boolean {
                val viewHolder = recyclerView?.findViewHolderForAdapterPosition(adapterPosition)
                // Check that the root view of the ViewHolder at the given adapter position has a descendant
                // whose id matches childViewId and whose status matches the supplied childViewMatcher.
                val matcher = hasDescendant(allOf(withId(childViewId), childViewMatcher))
                return viewHolder != null && matcher.matches(viewHolder.itemView)
            }
        }
    }
}
