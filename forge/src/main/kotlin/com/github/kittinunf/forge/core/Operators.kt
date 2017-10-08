package com.github.kittinunf.forge.core

infix fun <T, U> Function1<T, U>.map(deserializedResult: DeserializedResult<T>) = deserializedResult.map(this)

fun <T, U> DeserializedResult<(T) -> U>.apply(deserializedResult: DeserializedResult<T>): DeserializedResult<U> =
        when (this) {
            is DeserializedResult.Success -> deserializedResult.map(get())
            is DeserializedResult.Failure -> DeserializedResult.Failure(get())
        }

fun <T> JSON.at(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<T> {
    return find(key)?.deserializer() ?: DeserializedResult.Failure(PropertyNotFoundException(key))
}

fun <T> JSON.maybeAt(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<T> {
    return find(key)?.deserializer() ?: DeserializedResult.Success(null)
}

infix fun <T> JSON.at(key: String): DeserializedResult<T> = at(key) { valueAs<T>() }

infix fun <T> JSON.maybeAt(key: String): DeserializedResult<T> = maybeAt(key) { valueAs<T>() }

fun <T> JSON.list(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<List<DeserializedResult<T>>> {
    return find(key)?.map(deserializer)?.toList()?.let { DeserializedResult.Success(it) } ?:
            DeserializedResult.Failure(PropertyNotFoundException(key))
}

fun <T> JSON.maybeList(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<List<DeserializedResult<T>>> {
    return find(key)?.map(deserializer)?.toList()?.let { DeserializedResult.Success(it) } ?:
            DeserializedResult.Success(null)
}

fun <T> JSON.list(key: String) = list(key) { valueAs<T>() }

fun <T> JSON.maybeList(key: String) = list(key) { valueAs<T>() }
