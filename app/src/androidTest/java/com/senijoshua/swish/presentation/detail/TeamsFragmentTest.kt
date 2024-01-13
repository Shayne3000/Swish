package com.senijoshua.swish.presentation.detail

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.senijoshua.swish.R
import com.senijoshua.swish.data.MainRepository
import com.senijoshua.swish.shared_test.FakeMainRepository
import com.senijoshua.swish.util.TEAM_ID
import com.senijoshua.swish.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Instrumented integration test that verifies the
 * interaction of the [TeamsFragment] with the [TeamsViewModel].
 *
 * Create an instrumented unit test that verifies that the right UI elements show up on attachment.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TeamsFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // Fake or mock dependencies and values needed in the class
    @Inject
    lateinit var mainRepository: MainRepository

    private lateinit var fakeMainRepository: FakeMainRepository

    // Test the various states of the fragment
    @Before
    fun setUp() {
        hiltRule.inject()
        fakeMainRepository = mainRepository as FakeMainRepository
    }

    @Test
    fun teamsFragment_showsTeamDetail_onSuccessfulNetworkResponse() {
        // arrange
        val teamFragmentArgs = bundleOf(TEAM_ID to 1)
        launchFragmentInHiltContainer<TeamsFragment>(teamFragmentArgs)

        // act

        // assert
        onView(withId(R.id.teams_toolbar)).check(matches(isDisplayed()))
    }
}
