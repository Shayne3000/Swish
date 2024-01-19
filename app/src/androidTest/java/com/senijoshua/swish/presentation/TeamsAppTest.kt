package com.senijoshua.swish.presentation

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.senijoshua.swish.R
import com.senijoshua.swish.presentation.detail.TeamsActivity
import com.senijoshua.swish.presentation.list.MainActivity
import com.senijoshua.swish.shared_test.contentText
import com.senijoshua.swish.shared_test.fakeTeamData
import com.senijoshua.swish.shared_test.APP_TEXT
import com.senijoshua.swish.util.RecyclerViewItemMatcher
import com.senijoshua.swish.util.TEAM_ID
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented End-to-end test for the user journey of loading a list of NBA teams,
 * clicking on one, and navigating to the detail screen.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TeamsAppTest {
    // Setup hilt rule
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule(order = 2)
    val intentsTestRule = IntentsRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun teamsData_loadsIntoList_navigatesToDetail_andReturnsBack() {
        mainActivityVisibilityChecks()

        // scroll to the recyclerview item at a position and click it.
        onView(withId(R.id.nba_list)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click())
        )

        // check that an intent to start the teamsdetail activity was created
        intended(hasComponent(TeamsActivity::class.java.name))

        // Check that the intent that was created to start the teams activity has the right extras
        intended(hasExtra(TEAM_ID, 1))

        // check that the right team detail is visible
        detailFragmentVisibilityChecks()

        // press back and verify that we're back on the first activity
        Espresso.pressBack()

        // Verify that we're back on the main screen with the right data
        mainActivityVisibilityChecks()

        // simulate an activity recreation and verify that the main activity data is the same
        activityRule.scenario.recreate()

        mainActivityVisibilityChecks()
    }

    private fun mainActivityVisibilityChecks() {
        // check the toolbar text
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(APP_TEXT))))

        // scroll to a child in the recyclerview and check it is displayed
        onView(withId(R.id.nba_list)).check(
            matches(
                RecyclerViewItemMatcher.childViewOfItemAtPosition(
                    1,
                    R.id.team_name,
                    isDisplayed()
                )
            )
        )
    }

    private fun detailFragmentVisibilityChecks() {
        // check that the right team detail has what we're looking for.
        onView(withId(R.id.teams_toolbar)).check(matches(hasDescendant(withText(fakeTeamData[0].name))))

        onView(withId(R.id.teams_description)).check(matches(withText(contentText)))
    }
}
