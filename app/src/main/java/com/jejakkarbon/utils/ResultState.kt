package com.jejakkarbon.utils

sealed class ResultState<out T>(
    val data: T? = null,
    val message: String = ""
) {
    class Success<out T>(data: T) : ResultState<T>(data)
    class Loading<out T> : ResultState<T>()
    class Error<out T>(message: String, data: T? = null) : ResultState<T>(data, message)
}
