package com.senijoshua.swish.data

import com.squareup.moshi.JsonClass

/**
 * Generic representation of a network response
 * with parameterized generic types T and E, representing
 * the types of the deserialised success data and the error data respectively.
 */
sealed class ApiResponse<out T>(
    val success: T,
    val error: List<ApiError>
)

/**
 * Generic representation of an network response error type from the API.
 */
@JsonClass(generateAdapter = true)
data class ApiError(val required: String)

/**
 * Representation of the result of a data layer operation that is communicated to the presentation layer.
 */
sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    data class Error(val error: Throwable) : Result<Nothing>
    data object Loading : Result<Nothing> // Simply for validation/testing purposes.
}

/**
 * Generic parser that parses the network response to the type exposed to the presentation layer.
 */
fun <T> parseResponse(result: ApiResponse<T>): Result<T> {
    val successResponse = result.success
    val errorResponse = result.error

    return if (errorResponse.isEmpty()) {
        Result.Success(successResponse)
    } else {
        Result.Error(Throwable(errorResponse[0].required))
    }
}
