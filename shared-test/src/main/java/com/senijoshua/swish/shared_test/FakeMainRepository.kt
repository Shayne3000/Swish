package com.senijoshua.swish.shared_test

import com.senijoshua.swish.data.MainRepository
import com.senijoshua.swish.data.Result
import javax.inject.Inject

class FakeMainRepository @Inject constructor() : MainRepository {
    var shouldThrowError = false
    var shouldLoad = false

    override suspend fun loadTeams() = if (shouldThrowError) {
        Result.Error(Throwable(ERROR_TEXT))
    } else if (shouldLoad) {
        Result.Loading
    } else {
        Result.Success(fakeTeamsData)
    }

    override suspend fun getTeam(teamId: Int) = if (shouldThrowError) {
        Result.Error(Throwable(ERROR_TEXT))
    } else if (shouldLoad) {
        Result.Loading
    } else {
        Result.Success(fakeTeamData)
    }
}
