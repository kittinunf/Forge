package com.github.kttinunf.forge.core

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public fun <T, U> Function1<T, U>.map(result: Result<T>) = result.map(this)

public fun <T, U> Result<(T) -> U>.apply(result: Result<T>): Result<U> {
    when (this) {
        is Result.Success -> return result.map(get())
        is Result.Failure -> return Result.Failure(get())
    }
}

public fun <T> JSON.at(key: String, deserializer: JSON.() -> Result<T>): Result<T> {
    return find(key)?.
            let { it.deserializer() } ?:
            Result.Failure(PropertyNotFoundException(key))
}

public fun <T> JSON.maybeAt(key: String, deserializer: JSON.() -> Result<T>): Result<T> {
    return find(key)?.
            let { it.deserializer() } ?:
            Result.Success(null)
}

public fun <T> JSON.at(key: String): Result<T> = at(key) { valueAs<T>() }

public fun <T> JSON.maybeAt(key: String): Result<T> = maybeAt(key) { valueAs<T>() }

public fun <T> JSON.list(key: String, deserializer: JSON.() -> Result<T>): Result<List<Result<T>>> {
    return find(key)?.
            let { it.map(deserializer).toList() }?.
            let { Result.Success(it) } ?:
            Result.Failure<List<Result<T>>>(PropertyNotFoundException(key))
}

public fun <T> JSON.maybeList(key: String, deserializer: JSON.() -> Result<T>): Result<List<Result<T>>> {
    return find(key)?.
            let { it.map(deserializer).toList() }?.
            let { Result.Success(it) } ?:
            Result.Success(null)
}

public fun <T> JSON.list(key: String) = list(key) { valueAs<T>() }

public fun <T> JSON.maybeList(key: String) = list(key) { valueAs<T>() }
