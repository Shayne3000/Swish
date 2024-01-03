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
                parseResponse(api.getTeams())
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getTeam(teamId: Int): Result<Team> {
        return withContext(dispatcher) {
            try {
                parseResponse(api.getTeam(teamId))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}
