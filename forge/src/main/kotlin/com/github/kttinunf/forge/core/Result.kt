package com.github.kttinunf.forge.core

/**
 * Created by Kittinun Vantasin on 9/20/15.
 */

sealed public class Result<out T : Any?> {

    public operator abstract fun component1(): T?
    public operator abstract fun component2(): Exception?

    public fun fold(ft: (T?) -> Unit, fe: (Exception) -> Unit) {
        return when (this) {
            is Success<T> -> ft(this.value)
            is Failure<T> -> fe(this.error)
        }
    }

    public fun <U> map(f: (T) -> U): Result<U> {
        when (this) {
            is Result.Success -> return Result.Success(f(this.get()))
            is Result.Failure -> return Result.Failure(this.get())
        }
    }

    public fun <X> let(f: (T) -> X): X? {
        when (this) {
            is Success<T> -> return f(this.get())
            is Failure<T> -> return null
        }
    }

    public fun <X> get(): X {
        @Suppress("UNCHECKED_CAST")
        return when (this) {
            is Success<T> -> this.value as X
            is Failure<T> -> this.error as X
        }
    }

    public class Success<T : Any?>(val value: T?) : Result<T>() {

        public override fun component1() = value
        public override fun component2() = null

    }

    public class Failure<T : Any?>(val error: Exception) : Result<T>() {

        public override fun component1() = null
        public override fun component2() = error

    }

}