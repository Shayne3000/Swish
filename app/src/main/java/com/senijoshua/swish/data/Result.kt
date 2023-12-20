package com.senijoshua.swish.data

sealed interface Result<out T> {
    data class Success<out T>(val data: T) : Result<T>
    data class Error(val error: Throwable) : Result<Nothing>
    data object Loading : Result<Nothing> // Simply for validation/testing purposes.
}
