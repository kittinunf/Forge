package com.github.kttinunf.forge.core

/**
 * Created by Kittinun Vantasin on 9/20/15.
 */

sealed public class Result<out T : Any?, out E : Any> {

    public abstract fun component1(): T?
    public abstract fun component2(): E?

    public fun fold(ft: (T?) -> Unit, fe: (E) -> Unit) {
        return when (this) {
            is Success<T, E> -> ft(this.value)
            is Failure<T, E> -> fe(this.error)
        }
    }

    public fun <X> get(): X {
        @Suppress("UNCHECKED_CAST")
        return when (this) {
            is Success<T, E> -> this.value as X
            is Failure<T, E> -> this.error as X
        }
    }

    public class Success<T : Any?, E : Any>(val value: T?) : Result<T, E>() {

        public override fun component1() = value
        public override fun component2() = null

    }

    public class Failure<T : Any?, E : Any>(val error: E) : Result<T, E>() {

        public override fun component1() = null
        public override fun component2() = error

    }

}