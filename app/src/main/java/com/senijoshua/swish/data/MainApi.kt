package com.senijoshua.swish.data

import com.squareup.moshi.JsonClass
import retrofit2.http.GET

interface MainApi {
    @GET("teams")
    suspend fun getTeams(): TeamResponse
}

@JsonClass(generateAdapter = true)
data class TeamResponse(
    val response: List<Teams>
)

@JsonClass(generateAdapter = true)
data class Teams(val name: String, val logo: String? = null)
