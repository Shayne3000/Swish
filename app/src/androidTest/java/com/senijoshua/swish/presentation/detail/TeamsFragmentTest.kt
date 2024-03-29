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
import com.senijoshua.swish.shared_test.ERROR_TEXT
import com.senijoshua.swish.shared_test.FakeMainRepository
import com.senijoshua.swish.shared_test.contentText
import com.senijoshua.swish.shared_test.fakeTeamData
import com.senijoshua.swish.shared_test.launchFragmentInHiltContainer
import com.senijoshua.swish.util.TEAM_ID
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

        teamFragmentAssertions(isSuccessResponse = true)
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun teamsFragment_showsErrorSnackbar_onErrorNetworkResponse() {
        fakeMainRepository.shouldThrowError = true
        val teamFragmentArgs = bundleOf(TEAM_ID to 1)

        launchFragmentInHiltContainer<TeamsFragment>(teamFragmentArgs)

        teamFragmentAssertions(isSuccessResponse = false)
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
        onView(withText(ERROR_TEXT)).check(matches(isDisplayed()))
    }

    @Test
    fun teamsFragment_showsLoadingState_onLoadingNetworkResponse() {
        fakeMainRepository.shouldLoad = true
        val teamFragmentArgs = bundleOf(TEAM_ID to 1)

        launchFragmentInHiltContainer<TeamsFragment>(teamFragmentArgs)

        teamFragmentAssertions(isSuccessResponse = false)
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    /**
     * Tests that the fragment returns to the same lifecycle state
     * at which it was a priori after system-initiated process death
     * or a device orientation change and the like.
     */
    @Test
    fun teamsFragment_resumesProperly_onProcessDeathAndRecreation() {
        val teamFragmentArgs = bundleOf(TEAM_ID to 1)

        val containingActivityScenario = launchFragmentInHiltContainer<TeamsFragment>(teamFragmentArgs)

        teamFragmentAssertions(isSuccessResponse = true)

        containingActivityScenario.recreate()

        teamFragmentAssertions(isSuccessResponse = true)
    }

    private fun teamFragmentAssertions(isSuccessResponse: Boolean) {
        if (isSuccessResponse) {
            onView(withId(R.id.teams_toolbar)).check(matches(isDisplayed()))
            onView(withId(R.id.teams_toolbar)).check(matches(hasDescendant(withText(fakeTeamData[0].name))))

            onView(withContentDescription(R.string.team_logo)).check(matches(isDisplayed()))
            onView(withId(R.id.teams_description)).check(matches(withText(contentText)))
        } else {
            onView(withId(R.id.teams_toolbar)).check(matches(isDisplayed()))
            onView(withId(R.id.teams_toolbar)).check(matches(not(hasDescendant(withText(fakeTeamData[0].name)))))

            onView(withId(R.id.team_logo)).check(matches(isDisplayed()))
            onView(withId(R.id.teams_description)).check(matches(not(withText(contentText))))
        }
    }
}
