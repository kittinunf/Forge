package com.github.kittinunf.forge.core

sealed class DeserializedResult<out T> {

    operator abstract fun component1(): T?
    operator abstract fun component2(): Exception?

    inline fun fold(ft: (T?) -> Unit, fe: (Exception) -> Unit) {
        return when (this) {
            is Success<T> -> ft(this.value)
            is Failure<T> -> fe(this.error)
        }
    }

    inline fun <U> map(f: (T) -> U): DeserializedResult<U> = when (this) {
        is DeserializedResult.Success -> DeserializedResult.Success(f(get()))
        is DeserializedResult.Failure -> DeserializedResult.Failure(this.error)
    }

    inline fun <U> flatMap(fm: (T) -> DeserializedResult<U>): DeserializedResult<U> = when (this) {
        is DeserializedResult.Success -> fm(get())
        is DeserializedResult.Failure -> DeserializedResult.Failure(this.error)
    }

    @Suppress("UNCHECKED_CAST")
    fun <X> get(): X = when (this) {
        is Success<T> -> this.value as X
        is Failure<T> -> this.error as X
    }

    class Success<out T>(val value: T?) : DeserializedResult<T>() {

        override fun component1() = value
        override fun component2() = null

    }

    class Failure<out T>(val error: Exception) : DeserializedResult<T>() {

        override fun component1() = null
        override fun component2() = error

    }

}