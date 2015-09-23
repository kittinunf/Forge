package com.github.kttinunf.forge.core

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public fun <T, U> T?.fmap(f: (T) -> U?): U? {
    when (this) {
        null -> return null
        else -> return f(this)
    }
}

public fun <T, U> T?.unfold(fa: (T) -> U, fb: (T?) -> U): U {
    when (this) {
        null -> return fb(this)
        else -> return fa(this)
    }
}

public fun <T> JSON.at(key: String, deserializer: JSON.() -> Result<T, Exception>): Result<T, Exception> {
    return find(key).fmap(deserializer).unfold({
        it
    }, {
        Result.Failure(PropertyNotFoundException(key))
    })
}

public fun <T> JSON.at(key: String): Result<T, Exception> = at(key) { valueAs<T>() }

public fun <T> JSON.list(key: String, deserializer: JSON.() -> Result<T, Exception>): Result<List<Result<T, Exception>>, Exception> {
    return find(key).fmap { it.toList().map(deserializer) }.unfold({
        Result.Success<List<Result<T, Exception>>, Exception>(it)
    }, {
        Result.Failure<List<Result<T, Exception>>, Exception>(PropertyNotFoundException(key))
    })
}

public fun <T> JSON.list(key: String) = list(key) { valueAs<T>() }

public fun <T, U> Function1<T, U>.map(result: Result<T, Exception>): Result<U, Exception> {
    when (result) {
        is Result.Success -> return Result.Success(invoke(result.get()))
        is Result.Failure -> return Result.Failure(result.get())
    }
}

public fun <T, U> Result<(T) -> U, Exception>.apply(result: Result<T, Exception>): Result<U, Exception> {
    when (result) {
        is Result.Success -> return get<(T) -> U>().map(result)
        is Result.Failure -> return Result.Failure(result.get())
    }
}

internal fun <T: Any> JSON.modelFromJson(deserializer: JSON.() -> Result<T, Exception>) = this.deserializer()
internal fun <T: Any, U : Deserializable<T>> JSON.modelFromJson(deserializer: U) = deserializer.deserializer(this)
