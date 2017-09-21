package com.github.kttinunf.forge.core

sealed class EncodedResult<out T> {

    operator abstract fun component1(): T?
    operator abstract fun component2(): Exception?

    fun fold(ft: (T?) -> Unit, fe: (Exception) -> Unit) = when (this) {
        is Success<T> -> ft(this.value)
        is Failure<T> -> fe(this.error)
    }

    fun <U> map(f: (T) -> U): EncodedResult<U> {
        return when (this) {
            is EncodedResult.Success -> EncodedResult.Success(f(this.get()))
            is EncodedResult.Failure -> EncodedResult.Failure(this.get())
        }
    }

    fun <X> let(f: (T) -> X): X? {
        return when (this) {
            is Success<T> -> f(this.get())
            is Failure<T> -> null
        }
    }

    fun <X> get(): X {
        @Suppress("UNCHECKED_CAST")
        return when (this) {
            is Success<T> -> this.value as X
            is Failure<T> -> this.error as X
        }
    }

    class Success<out T : Any?>(val value: T) : EncodedResult<T>() {

        override fun component1() = value
        override fun component2() = null

    }

    class Failure<out T : Any?>(val error: Exception) : EncodedResult<T>() {

        override fun component1() = null
        override fun component2() = error

    }

}