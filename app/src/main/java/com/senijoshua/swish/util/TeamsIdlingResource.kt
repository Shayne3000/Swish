package com.senijoshua.swish.util

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * IdlingResource for the loadTeams network request
 * that will be registered with Espresso so to be able to
 * synchronize said async operation within instrumentation tests.
 */
object TeamsIdlingResource {
    private const val RESOURCE = "TEAMS_REQUEST"

    val idlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        idlingResource.increment()
    }

    fun decrement() {
        if (!idlingResource.isIdleNow) {
            idlingResource.decrement()
        }
    }

}
