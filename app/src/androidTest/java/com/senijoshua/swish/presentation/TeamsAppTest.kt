package com.senijoshua.swish.presentation

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.senijoshua.swish.presentation.list.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented End-to-end test for the user journey of loading a list of NBA teams,
 * clicking on one and navigating to the detail screen.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TeamsAppTest {
    // Setup hilt rule
    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun teamsData_loadsIntoList_navigatesToDetail_andReturnsBack() {
        // arrange

        // check the toolbar text

        // scroll to an descendant in the recyclerview and click it

        // check that an intent to start the teamsdetail activity was created

        // check that the right team detail is visible and has what we're looking for.

        // press back and verify that we're back on the first activity

        // simulate an activity recreation and verify that the data is the same. 

        // act

        // assert
    }
    // Test cases the various cases of the user flow of starting the app, loading team data and navigating to the detail screen
}
