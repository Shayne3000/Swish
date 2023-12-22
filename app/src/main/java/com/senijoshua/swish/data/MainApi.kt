package com.senijoshua.swish.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Path

interface MainApi {
    @GET("teams")
    suspend fun getTeams(): TeamResponse

    @GET("teams/{id}")
    suspend fun getTeam(@Path("id") id: Int): TeamResponse
}

@JsonClass(generateAdapter = true)
data class TeamResponse(
    val response: List<Teams>
)

@JsonClass(generateAdapter = true)
data class Teams(
    val id: Int,
    val name: String,
    @Json(name = "nationnal")
    val national: Boolean,
    val logo: String? = null
)
