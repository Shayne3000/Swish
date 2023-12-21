package com.senijoshua.swish.presentation

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.senijoshua.swish.R
import com.senijoshua.swish.data.MainRepository
import com.senijoshua.swish.presentation.list.MainActivity
import com.senijoshua.swish.shared_test.FakeMainRepository
import com.senijoshua.swish.util.RecyclerViewItemMatcher.childViewOfItemAtPosition
import com.senijoshua.swish.util.TeamsIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Instrumented integration test for testing the [MainActivity] and its interaction with the
 * [MainViewModel].
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    // Setup Hilt Rule
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    // Mock/Fake/Inject dependencies
    // In truth, the injected implementation here is FakeMainRepository. We officially cast it later.
    @Inject
    lateinit var mainRepository: MainRepository

    private lateinit var fakeMainRepository: FakeMainRepository

    private val toolbarName = "NBA List"

    @Before
    fun setUp() {
        hiltRule.inject()
        fakeMainRepository = mainRepository as FakeMainRepository
    }

    // Add test cases that test the behaviour of the various states of the UI
    @Test
    fun mainScreen_showsLoadingState_onLoad() {
        // NB: For the loading state, we do not need an idling resource, as
        // Espresso need not know of or wait for any asynchronous operation to complete here.
        fakeMainRepository.shouldLoad = true

        // We do not use an ActivityScenarioRule because we want more control over the Activity during the test
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withText(toolbarName)).check(matches(isDisplayed()))
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))

        scenario.close()
    }

    @Test
    fun nbaList_showsTeamsList_onLoadSuccess() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        // This is for validation purposes as in truth we do not need an IdlingResource since we're
        // using a fake implementation of  the repository with a synchronous call.
        // Fakes can be used to synchronize the test thread instead of an idling resource
        IdlingRegistry.getInstance().register(TeamsIdlingResource.idlingResource)

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withText(toolbarName)).check(matches(isDisplayed()))

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

        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))

        IdlingRegistry.getInstance().unregister(TeamsIdlingResource.idlingResource)
        scenario.close()
    }

    @Test
    fun nbaList_showsErrorSnackBar_onLoadFailure() {
        fakeMainRepository.shouldThrowError = true
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
        onView(withText(toolbarName)).check(matches(isDisplayed()))

        onView(withText("Error!")).check(matches(isDisplayed()))
        // check that the child of the recyclerview at the given position is not displayed
        onView(withId(R.id.nba_list)).check(
            matches(
                not(
                    childViewOfItemAtPosition(
                        1,
                        R.id.team_thumbnail,
                        isDisplayed()
                    )
                )
            )
        )
        onView(withId(R.id.nba_list)).check(
            matches(
                not(
                    childViewOfItemAtPosition(
                        1,
                        R.id.team_name,
                        isDisplayed()
                    )
                )
            )
        )
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())))

        scenario.close()
    }
}
