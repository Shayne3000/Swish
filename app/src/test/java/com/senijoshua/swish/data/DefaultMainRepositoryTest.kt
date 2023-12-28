package com.senijoshua.swish.data

import com.senijoshua.swish.shared_test.fakeTeamData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
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
    fun `loadTeams returns teams list on successful network request`() = runTest(dispatcher) {
        // arrange
        whenever(apiService.getTeams()).thenReturn(TeamsResponse(fakeTeamData))

        // act
        val result = repository.loadTeams()

        // assert
        check(result is Result.Success)
        assertEquals(fakeTeamData[0].name, result.data[0].name)
        assertEquals(fakeTeamData.size, result.data.size)
    }

    @Test(expected = Throwable::class)
    fun `loadTeams returns error on network request error`() = runTest(dispatcher) {
        // arrange
        whenever(apiService.getTeams()).thenThrow(Throwable("error"))

        // act
        val result = repository.loadTeams()

        // assert
        check(result is Result.Error)
        assertEquals("error", result.error.message)
    }
}
