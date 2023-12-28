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
                val result = api.getTeams()
                val response = result.response
                val error = result.errors

                if (error.isEmpty()) {
                    Result.Success(response)
                } else {
                    Result.Error(Throwable(error[0].required))
                }
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getTeam(teamId: Int): Result<Team> {
        return withContext(dispatcher) {
            try {
                val response = api.getTeam(teamId).response
                Result.Success(response[0])
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}
