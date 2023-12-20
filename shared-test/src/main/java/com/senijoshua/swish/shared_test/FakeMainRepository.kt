package com.senijoshua.swish.shared_test

import com.senijoshua.swish.data.MainRepository
import com.senijoshua.swish.data.Result
import com.senijoshua.swish.data.Teams
import javax.inject.Inject

class FakeMainRepository @Inject constructor() : MainRepository {
    var shouldThrowError = false
    var shouldLoad = false

    override suspend fun loadTeams(): Result<List<Teams>> = if (shouldThrowError) {
        Result.Error(Throwable("Error!"))
    } else if (shouldLoad) {
        Result.Loading
    } else {
        Result.Success(fakeTeamData)
    }
}
