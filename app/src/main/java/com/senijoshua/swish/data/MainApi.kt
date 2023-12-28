package com.senijoshua.swish.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

interface MainApi {
    @GET("teams")
    suspend fun getTeams(): TeamsResponse

    @GET("https://v1.basketball.api-sports.io/teams")
    suspend fun getTeam(@Query("id") id: Int): TeamResponse
}

@JsonClass(generateAdapter = true)
data class TeamsResponse(
    val response: List<Teams>,
    val errors: List<TeamError> = emptyList()
)

@JsonClass(generateAdapter = true)
data class TeamResponse(
    val response: List<Team>
)

@JsonClass(generateAdapter = true)
data class Teams(
    val id: Int,
    val name: String,
    val allStar: Boolean,
    val logo: String? = null
)

@JsonClass(generateAdapter = true)
data class Team(
    val id: Int,
    val name: String,
    @Json(name = "nationnal")
    val national: Boolean,
    val logo: String? = null
)

@JsonClass(generateAdapter = true)
data class TeamError(val required: String)
