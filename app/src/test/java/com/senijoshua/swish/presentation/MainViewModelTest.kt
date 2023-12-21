package com.senijoshua.swish.presentation

import com.senijoshua.swish.data.DefaultMainRepository
import com.senijoshua.swish.data.Result
import com.senijoshua.swish.presentation.list.MainState
import com.senijoshua.swish.presentation.list.MainViewModel
import com.senijoshua.swish.shared_test.fakeTeamData
import com.senijoshua.swish.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class MainViewModelTest {
    // Setup dispatcher rule to replace the main dispatcher with the test one.
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // mock or fake dependencies
    private val repository: DefaultMainRepository = mock()

    // setup subject under test
    private lateinit var vm: MainViewModel

    @Before
    fun setUp() {
        vm = MainViewModel(repository)
    }

    // Setup test cases that verify the user's state
    @Test
    fun `ui state is in loading state on initialization`() = runTest {
        assertEquals(MainState.Loading, vm.uiState.value)
    }

    @Test
    fun `getTeams returns the list of teams on success`() = runTest {
        // arrange
        whenever(repository.loadTeams()).thenReturn(Result.Success(fakeTeamData))

        // act
        vm.getTeams()

        // assert
        assertEquals(MainState.Success(fakeTeamData), vm.uiState.value)
    }

    @Test
    fun `getTeams returns error on failure`() = runTest {
        // arrange
        whenever(repository.loadTeams()).thenReturn(Result.Error(Throwable("error")))

        // act
        vm.getTeams()

        // assert
        assertEquals(MainState.Error("error"), vm.uiState.value)
    }
}
