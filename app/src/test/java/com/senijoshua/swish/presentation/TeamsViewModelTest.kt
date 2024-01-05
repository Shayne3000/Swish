package com.senijoshua.swish.presentation

import androidx.lifecycle.SavedStateHandle
import com.senijoshua.swish.data.DefaultMainRepository
import com.senijoshua.swish.data.Result
import com.senijoshua.swish.presentation.detail.Error
import com.senijoshua.swish.presentation.detail.Loading
import com.senijoshua.swish.presentation.detail.Success
import com.senijoshua.swish.presentation.detail.TeamsViewModel
import com.senijoshua.swish.shared_test.fakeTeamData
import com.senijoshua.swish.util.MainDispatcherRule
import com.senijoshua.swish.util.TEAM_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class TeamsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repo: DefaultMainRepository = mock()

    private val savedState = SavedStateHandle(mapOf(TEAM_ID to 1))

    private lateinit var vm: TeamsViewModel

    @Before
    fun setUp() {
        vm = TeamsViewModel(repo, savedState)
    }

    @Test
    fun `UiState is in loading state on initialisation`() = runTest {
        assertEquals(Loading, vm.uiState.value)
    }

    @Test
    fun `getTeam returns loading whilst the network request`() = runTest {
        whenever(repo.getTeam(anyInt())).thenReturn(Result.Loading)

        vm.getTeam()

        assertEquals(Loading, vm.uiState.value)
    }

    @Test
    fun `getTeam returns team on successful network response`() = runTest {
        whenever(repo.getTeam(anyInt())).thenReturn(Result.Success(fakeTeamData))

        vm.getTeam()

        assertEquals(Success(fakeTeamData[0]), vm.uiState.value)
    }

    @Test
    fun `getTeam returns error on error network response`() = runTest {
        val errorMessage = "Error!"
        whenever(repo.getTeam(anyInt())).thenReturn(Result.Error(Throwable(errorMessage)))

        vm.getTeam()

        assertEquals(Error(errorMessage), vm.uiState.value)
    }
}
