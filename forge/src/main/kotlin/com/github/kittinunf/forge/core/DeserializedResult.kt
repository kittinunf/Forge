package com.github.kittinunf.forge.core

sealed class DeserializedResult<out T> {

    abstract operator fun component1(): T?
    abstract operator fun component2(): ForgeError?

    inline fun <U> map(f: (T) -> U): DeserializedResult<U> = when (this) {
        is Success -> Success(f(get()))
        is Failure -> Failure(error)
    }

    inline fun <U> flatMap(fm: (T) -> DeserializedResult<U>): DeserializedResult<U> = when (this) {
        is Success -> fm(get())
        is Failure -> Failure(error)
    }

    @Suppress("UNCHECKED_CAST")
    fun <X> get(): X = when (this) {
        is Success -> value as X
        is Failure -> error("${javaClass.simpleName} is a Success state: $this")
    }

    fun error(): ForgeError = when (this) {
        is Success -> error("${javaClass.simpleName} is a Failure state: $this")
        is Failure -> error
    }

    class Success<out T>(val value: T?) : DeserializedResult<T>() {

        override fun component1() = value
        override fun component2() = null

        override fun toString() = "[Success($value)]"
    }

    class Failure<out T>(val error: ForgeError) : DeserializedResult<T>() {

        override fun component1() = null
        override fun component2() = error

        override fun toString() = "[Failure($error)]"
    }
}
