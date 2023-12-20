package com.senijoshua.swish.data

interface MainRepository {
    suspend fun loadTeams(): Result<List<Teams>>
}
