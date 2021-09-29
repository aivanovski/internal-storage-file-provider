package com.github.ai.fprovider.entity

// This class is necessary because of kotlin.Result has @JvmInline annotation
// which produces errors in tests
internal sealed class Result<out T : Any> {

    val isSuccess: Boolean
        get() = this is Success

    val isFailure: Boolean
        get() = this is Failure

    fun getOrThrow(): T = (this as? Success)?.value ?: throw IllegalStateException()

    fun getExceptionOrThrow(): Throwable = (this as? Failure)?.exception ?: IllegalStateException()

    inline fun <R : Any> map(transform: (value: T) -> R): Result<R> {
        return when {
            isSuccess -> Success(transform.invoke(getOrThrow()))
            else -> Failure(exception = getExceptionOrThrow())
        }
    }

    fun <R : Any> takeError(): Result<R> {
        return when {
            isFailure -> Failure(exception = getExceptionOrThrow())
            else -> throw IllegalStateException()
        }
    }

    data class Success<out T : Any>(val value: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()
}