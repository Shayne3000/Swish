package com.senijoshua.swish.util

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.core.util.Preconditions
import androidx.fragment.app.Fragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.senijoshua.swish.HiltTestActivity
import com.senijoshua.swish.R

/**
 * launchFragmentInContainer from the androidx.fragment:fragment-testing library
 * is unsuitable for use with Hilt as it throws an ILE because it uses a hardcoded empty Activity
 * under the hood (i.e. [EmptyFragmentActivity]) that is not annotated with @AndroidEntryPoint.
 *
 * This is a workaround that uses an empty, @AndroidEntryPoint-annotated [HiltTestActivity] as the containing
 * activity to whose root view controller, the fragment under test will be attached.
 *
 * Create the empty, @AndroidEntryPoint-annotated [HiltTestActivity] in a debug source set within the app module
 * and include it in an AndroidManifest.xml file within the same source set.
 */
inline fun <reified T : Fragment> launchFragmentInHiltContainer(
    fragmentArgs: Bundle? = null,
    @StyleRes themeResId: Int = R.style.Theme_Swish,
    crossinline action: Fragment.() -> Unit = {}
): ActivityScenario<HiltTestActivity> {
    val startActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId
    )

    val activityScenario = ActivityScenario.launch<HiltTestActivity>(startActivityIntent).onActivity { activity ->
        val fragment: Fragment = activity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs

        activity.supportFragmentManager
            .beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()

        fragment.action()
    }

    return activityScenario
}
