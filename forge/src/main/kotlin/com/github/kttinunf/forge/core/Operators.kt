package com.github.kttinunf.forge.core

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public fun <T, U> Result<T, Exception>.map(f: (T) -> U): Result<U, Exception> {
    when (this) {
        is Result.Success -> return Result.Success(f(get()))
        is Result.Failure -> return Result.Failure(get())
    }
}

public fun <T, U> Function1<T, U>.map(result: Result<T, Exception>) = result.map(this)

public fun <T, U> Result<(T) -> U, Exception>.apply(result: Result<T, Exception>): Result<U, Exception> {
    when (this) {
        is Result.Success -> return result.map(get())
        is Result.Failure -> return Result.Failure(get())
    }
}

public fun <T> JSON.at(key: String, deserializer: JSON.() -> Result<T, Exception>): Result<T, Exception> {
    return find(key)?.
            let { it.deserializer() } ?:
            Result.Failure(PropertyNotFoundException(key))
}

public fun <T> JSON.maybeAt(key: String, deserializer: JSON.() -> Result<T, Exception>): Result<T, Exception> {
    return find(key)?.
            let { it.deserializer() } ?:
            Result.Success(null)
}

public fun <T> JSON.at(key: String): Result<T, Exception> = at(key) { valueAs<T>() }

public fun <T> JSON.maybeAt(key: String): Result<T, Exception> = maybeAt(key) { valueAs<T>() }

public fun <T> JSON.list(key: String, deserializer: JSON.() -> Result<T, Exception>): Result<List<Result<T, Exception>>, Exception> {
    return find(key)?.
            let { it.toList().map(deserializer) }?.
            let { Result.Success<List<Result<T, Exception>>, Exception>(it) } ?:
            Result.Failure<List<Result<T, Exception>>, Exception>(PropertyNotFoundException(key))
}

public fun <T> JSON.list(key: String) = list(key) { valueAs<T>() }
