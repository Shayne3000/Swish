package com.senijoshua.swish.presentation.detail

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.senijoshua.swish.R
import com.senijoshua.swish.data.MainRepository
import com.senijoshua.swish.shared_test.FakeMainRepository
import com.senijoshua.swish.shared_test.fakeTeamData
import com.senijoshua.swish.util.TEAM_ID
import com.senijoshua.swish.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Instrumented integration test that verifies the
 * interaction of the [TeamsFragment] with the [TeamsViewModel]
 * and the [MainRepository] transitively.
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

    private val contentText = "The id of the team is ${fakeTeamData[0].id}, the name of team is " +
            "${fakeTeamData[0].name}, and the state of its national is ${fakeTeamData[0].national}."

    // Test the various states of the fragment
    @Before
    fun setUp() {
        hiltRule.inject()
        fakeMainRepository = mainRepository as FakeMainRepository
    }

    @Test
    fun teamsFragment_showsTeamDetail_onSuccessfulNetworkResponse() {
        val teamFragmentArgs = bundleOf(TEAM_ID to 1)
        launchFragmentInHiltContainer<TeamsFragment>(teamFragmentArgs)

        onView(withId(R.id.teams_toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.teams_toolbar)).check(matches(hasDescendant(withText(fakeTeamData[0].name))))

        onView(withContentDescription(R.string.team_logo)).check(matches(isDisplayed()))
        onView(withId(R.id.teams_description)).check(matches(withText(contentText)))

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun teamsFragment_showsErrorSnackbar_onErrorNetworkResponse() {
        fakeMainRepository.shouldThrowError = true
        val teamFragmentArgs = bundleOf(TEAM_ID to 1)

        launchFragmentInHiltContainer<TeamsFragment>(teamFragmentArgs)

        onView(withId(R.id.teams_toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.teams_toolbar)).check(matches(not(hasDescendant(withText(fakeTeamData[0].name)))))

        onView(withId(R.id.team_logo)).check(matches(isDisplayed()))
        onView(withId(R.id.teams_description)).check(matches(not(withText(contentText))))

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withText(fakeMainRepository.errorMessage)).check(matches(isDisplayed()))
    }

    @Test
    fun teamsFragment_showsLoadingState_onLoadingNetworkResponse() {
        fakeMainRepository.shouldLoad = true
        val teamFragmentArgs = bundleOf(TEAM_ID to 1)

        launchFragmentInHiltContainer<TeamsFragment>(teamFragmentArgs)

        onView(withId(R.id.teams_toolbar)).check(matches(isDisplayed()))
        onView(withId(R.id.teams_toolbar)).check(matches(not(hasDescendant(withText(fakeTeamData[0].name)))))

        onView(withId(R.id.team_logo)).check(matches(isDisplayed()))
        onView(withId(R.id.teams_description)).check(matches(not(withText(contentText))))

        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }
}
