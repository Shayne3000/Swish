package com.senijoshua.swish.data

import com.senijoshua.swish.shared_test.fakeTeamData
import com.senijoshua.swish.shared_test.fakeTeamsData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class DefaultMainRepositoryTest {
    // Setup test dispatcher and perhaps a test scope
    private val dispatcher = UnconfinedTestDispatcher()

    // fake/mock dependencies and any dummy data
    private val apiService: MainApi = mock()

    // Setup subject under test
    private lateinit var repository: DefaultMainRepository

    @Before
    fun setUp() {
        repository = DefaultMainRepository(apiService, dispatcher)
    }

    @Test
    fun `loadTeams returns teams list on successful network response`() = runTest(dispatcher) {
        whenever(apiService.getTeams()).thenReturn(TeamsResponse(fakeTeamsData))

        val result = repository.loadTeams()

        check(result is Result.Success)
        assertEquals(fakeTeamsData[0].name, result.data[0].name)
        assertEquals(fakeTeamsData.size, result.data.size)
    }

    @Test
    fun `loadTeams returns ApiError on error network response`() = runTest(dispatcher) {
        val errorMessage = "Error!"
        whenever(apiService.getTeams()).thenReturn(
            TeamsResponse(
                emptyList(),
                listOf(ApiError(required = errorMessage))
            )
        )

        val result = repository.loadTeams()

        check(result is Result.Error)
        assertEquals(errorMessage, result.error.message)
    }

    @Test(expected = Throwable::class)
    fun `loadTeams returns error on network request failure`() = runTest(dispatcher) {
        whenever(apiService.getTeams()).thenThrow(Throwable("error"))

        val result = repository.loadTeams()

        check(result is Result.Error)
        assertEquals("error", result.error.message)
    }

    @Test
    fun `getTeam returns a team on successful network respoonse`() = runTest(dispatcher) {
        whenever(apiService.getTeam(anyInt())).thenReturn(TeamResponse(fakeTeamData))

        val result = repository.getTeam(1)

        check(result is Result.Success)
        assertEquals(fakeTeamData[0], result.data[0])
    }

    @Test
    fun `getTeam returns ApiError on error network response`() = runTest(dispatcher) {
        val errorMessage = "Error!"
        whenever(apiService.getTeam(anyInt())).thenReturn(
            TeamResponse(
                response = emptyList(), errors = listOf(
                    ApiError(required = errorMessage)
                )
            )
        )

        val result = repository.getTeam(1)

        check(result is Result.Error)
        assertEquals(errorMessage, result.error.message)
    }

    @Test(expected = Throwable::class)
    fun `getTeam returns an error on network request failure`() = runTest(dispatcher) {
        val exception = "IOException"
        whenever(apiService.getTeam(anyInt())).thenThrow(Throwable(exception))

        val result = repository.getTeam(1)

        check(result is Result.Error)
        assertEquals(exception, result.error.message)
    }
}
