package com.senijoshua.swish.presentation

import androidx.core.os.bundleOf
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.senijoshua.swish.R
import com.senijoshua.swish.data.MainRepository
import com.senijoshua.swish.presentation.detail.TeamsFragment
import com.senijoshua.swish.shared_test.FakeMainRepository
import com.senijoshua.swish.shared_test.fakeTeamData
import com.senijoshua.swish.shared_test.launchFragmentInHiltContainer
import com.senijoshua.swish.util.TEAM_ID
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

/**
 * Local integration test verifying the interaction of the Teams Fragment
 * with the [TeamsViewModel].
 */
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class TeamsFragmentRobolectricTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mainRepository: MainRepository

    private lateinit var fakeMainRepository: FakeMainRepository

    private val contentText = "The id of the team is ${fakeTeamData[0].id}, the name of team is " +
            "${fakeTeamData[0].name}, and the state of its national is ${fakeTeamData[0].national}."

    private val fragmentArgs = bundleOf(TEAM_ID to 1)

    @Before
    fun setUp() {
        hiltRule.inject()
        fakeMainRepository = mainRepository as FakeMainRepository
    }

    @Test
    fun teamsFragment_showsTeam_onSuccessfulNetworkRequest() {
        launchFragmentInHiltContainer<TeamsFragment>(fragmentArgs)

        onView(withId(R.id.teams_toolbar)).check(matches(isDisplayed()))

        onView(withId(R.id.teams_toolbar)).check(matches(hasDescendant(withText(fakeTeamData[0].name))))

        onView(withContentDescription(R.string.team_logo)).check(matches(isDisplayed()))

        onView(withId(R.id.teams_description)).check(matches(withText(contentText)))

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))
    }

    @Test
    fun teamsFragment_showError_onNetworkRequestErrorResponse() {
        // arrange
        fakeMainRepository.shouldThrowError = true

        launchFragmentInHiltContainer<TeamsFragment>(fragmentArgs)

        // act
        onView(withId(R.id.teams_toolbar)).check(matches(isDisplayed()))

        onView(withId(R.id.teams_toolbar)).check(matches(not(hasDescendant(withText(fakeTeamData[0].name)))))

        // assert
        onView(withId(R.id.teams_description)).check(matches(not(withText(contentText))))

        onView(withText(fakeMainRepository.errorMessage)).check(matches(isDisplayed()))
    }
}
