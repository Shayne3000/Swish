package com.senijoshua.swish.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: MainApi,
    private val dispatcher: CoroutineDispatcher
): MainRepository {
    override suspend fun loadTeams(): Result<List<Teams>> {
        return withContext(dispatcher) {
            try {
                val result = api.getTeams()
                Result.Success(result.response)
                // ideally check if response is null, if so check if result.Error is not null
                // If not null, return Result.Error(Throwable(errorDataModel.message))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }
}
