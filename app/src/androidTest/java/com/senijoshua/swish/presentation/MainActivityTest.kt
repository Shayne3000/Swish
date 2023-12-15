package com.senijoshua.swish.presentation

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.senijoshua.swish.R
import com.senijoshua.swish.util.RecyclerViewItemMatcher.childViewOfItemAtPosition
import com.senijoshua.swish.util.TeamsIdlingResource
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented integration test for testing the [MainActivity] and its interaction with the
 * [MainViewModel].
 * Perhaps this should be an integration test where we verify the interaction of the
 * Activity with the ViewModel.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    // Consider setting up a hilt rule

    // Mock/Fake dependencies

    // Setup rule to launch the Activity under test before each test case and close it afterwards
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private val toolbarName = "NBA List"

    // Add test cases that test the behaviour of the various states of the UI
    @Test
    fun mainScreen_showsLoadingState_onLoad() {
        // NB: For the loading state, we do not need an idling resource,
        // Espresso should not know of or wait for any asynchronous operation to complete.
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withText(toolbarName)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun nbaList_showsTeamsList_onLoadSuccess() {
        // arrange
        IdlingRegistry.getInstance().register(TeamsIdlingResource.idlingResource)
        onView(withText(toolbarName)).check(matches(isDisplayed()))

        // act
        // assert that a child of the root view of the ViewHolder at a given recyclerview adapter position is displayed
        onView(withId(R.id.nba_list)).check(
            matches(
                childViewOfItemAtPosition(
                    1,
                    R.id.team_thumbnail,
                    isDisplayed()
                )
            )
        )

        onView(withId(R.id.nba_list)).check(
            matches(
                childViewOfItemAtPosition(
                    1,
                    R.id.team_name,
                    isDisplayed()
                )
            )
        )

        IdlingRegistry.getInstance().unregister(TeamsIdlingResource.idlingResource)
    }

    @Test
    fun nbaList_showsErrorSnackBar_onLoadFailure() {

    }

    // Create a fake repository, use hilt to replace the original main repository with the fake and use that to tweak the behaviour of the data layer

    // Separate Integration test with hilt that mocks the repository, injects it into the viewmodel and tests its interaction with the view
    // End TO End test with hilt and idling resource
}
