package com.senijoshua.swish.presentation

import android.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_6
import app.cash.paparazzi.Paparazzi
import com.senijoshua.swish.R
import com.senijoshua.swish.presentation.list.MainAdapter
import com.senijoshua.swish.shared_test.fakeTeamsData
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * A snapshot/screenshot test for ensuring that the UI of the activity
 * matches expectations and guarding against visual regressions.
 */
class MainActivitySnapshotTest {
    // Setup paparazzi rule
    @get:Rule
    val paparazzi =
        Paparazzi(deviceConfig = PIXEL_6)

    // Setup dependencies required to inflate the Activity's layout elements.
    private var adapter: MainAdapter = MainAdapter(){}
    private lateinit var rootView: ConstraintLayout
    private lateinit var toolbar: Toolbar
    private lateinit var nbaList: RecyclerView

    @Before
    fun setUp() {
        // Setup activity layout elements
        rootView = paparazzi.inflate(R.layout.activity_main)
        toolbar = rootView.findViewById(R.id.toolbar)
        nbaList = rootView.findViewById(R.id.nba_list)
    }

    // Take screen shot of the activity

    // then verify that the screenshot matches what we expect.
    @Test
    fun launchMainActivityLayout_LightTheme() {
        paparazzi.unsafeUpdateConfig(theme = "android:Theme.Material3.Light.NoActionBar")
        inflateLayoutElements()
        paparazzi.snapshot(view = rootView, name = "light_mode")
    }

    @Test
    fun launchMainActivityLayout_DarkTheme() {
        paparazzi.unsafeUpdateConfig(theme = "android:Theme.Material3.Dark.NoActionBar")
        inflateLayoutElements()
        paparazzi.snapshot(view = rootView, name = "dark_mode")
    }

    private fun inflateLayoutElements() {
        toolbar.title = rootView.context.getString(R.string.app_name)
        toolbar.setTitleTextColor(rootView.context.getColor(R.color.white))

        nbaList.adapter = adapter
        nbaList.layoutManager = LinearLayoutManager(rootView.context)
        nbaList.addItemDecoration(
            DividerItemDecoration(
                rootView.context,
                LinearLayoutManager.VERTICAL
            )
        )

        adapter.submitList(fakeTeamsData)
    }
}
