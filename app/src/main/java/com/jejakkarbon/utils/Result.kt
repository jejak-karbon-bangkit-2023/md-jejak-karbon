package com.jejakkarbon.utils

sealed class Result<out T>(
    val data: T? = null,
    val message: String = ""
) {
    class Success<out T>(data: T) : Result<T>(data)
    class Loading<out T> : Result<T>()
    class Error<out T>(message: String, data: T? = null) : Result<T>(data, message)
}
