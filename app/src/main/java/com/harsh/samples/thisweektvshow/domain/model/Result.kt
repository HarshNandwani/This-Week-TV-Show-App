package com.harsh.samples.thisweektvshow.domain.model

// inspired from https://www.linkedin.com/feed/update/urn:li:activity:7150818581406994432/
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val exception: Exception) : Result<Nothing>()

    fun onSuccess(block: (T) -> Unit): Result<T> {
        if (this is Success) {
            block(this.data)
        }
        return this
    }

    fun onFailure(block: (Exception) -> Unit): Result<T> {
        if (this is Failure) {
            block(this.exception)
        }
        return this
    }
}
