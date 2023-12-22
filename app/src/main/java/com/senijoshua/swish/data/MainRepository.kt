package com.senijoshua.swish.data

interface MainRepository {
    suspend fun loadTeams(): Result<List<Teams>>

    suspend fun getTeam(teamId: Int): Result<Teams>
}
