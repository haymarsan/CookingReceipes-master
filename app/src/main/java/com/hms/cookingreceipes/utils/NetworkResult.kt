package com.hms.cookingreceipes.utils


sealed class NetworkResult<T>(val data: T? = null, val message: String? = null) {
    class Loading<T> : NetworkResult<T>()
    class Success<T>(data: T?) : NetworkResult<T>(data, null)
    class Error<T>(message: String?) : NetworkResult<T>(null, message)
}