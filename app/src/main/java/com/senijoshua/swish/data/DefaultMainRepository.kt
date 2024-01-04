package com.senijoshua.swish.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: MainApi,
    private val dispatcher: CoroutineDispatcher
) : MainRepository {
    override suspend fun loadTeams(): Result<List<Teams>> {
        return withContext(dispatcher) {
            try {
                //on Successful network respone
                parseResponseToResult(api.getTeams())
            } catch (e: Exception) {
                // Network error like network request failure
                Result.Error(e)
            }
        }
    }

    override suspend fun getTeam(teamId: Int): Result<Team> {
        return withContext(dispatcher) {
            try {
                parseResponseToResult(api.getTeam(teamId))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}
