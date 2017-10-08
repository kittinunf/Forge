package com.github.kittinunf.forge.core

infix fun <T, U> Function1<T, U>.map(encodedResult: EncodedResult<T>) = encodedResult.map(this)

fun <T, U> EncodedResult<(T) -> U>.apply(encodedResult: EncodedResult<T>): EncodedResult<U> =
        when (this) {
            is EncodedResult.Success -> encodedResult.map(get())
            is EncodedResult.Failure -> EncodedResult.Failure(get())
        }

fun <T> JSON.at(key: String, deserializer: JSON.() -> EncodedResult<T>): EncodedResult<T> {
    return find(key)?.deserializer() ?: EncodedResult.Failure(PropertyNotFoundException(key))
}

fun <T> JSON.maybeAt(key: String, deserializer: JSON.() -> EncodedResult<T>): EncodedResult<T> {
    return find(key)?.deserializer() ?: EncodedResult.Success(null)
}

infix fun <T> JSON.at(key: String): EncodedResult<T> = at(key) { valueAs<T>() }

infix fun <T> JSON.maybeAt(key: String): EncodedResult<T> = maybeAt(key) { valueAs<T>() }

fun <T> JSON.list(key: String, deserializer: JSON.() -> EncodedResult<T>): EncodedResult<List<EncodedResult<T>>> {
    return find(key)?.map(deserializer)?.toList()?.let { EncodedResult.Success(it) } ?:
            EncodedResult.Failure(PropertyNotFoundException(key))
}

fun <T> JSON.maybeList(key: String, deserializer: JSON.() -> EncodedResult<T>): EncodedResult<List<EncodedResult<T>>> {
    return find(key)?.map(deserializer)?.toList()?.let { EncodedResult.Success(it) } ?:
            EncodedResult.Success(null)
}

fun <T> JSON.list(key: String) = list(key) { valueAs<T>() }

fun <T> JSON.maybeList(key: String) = list(key) { valueAs<T>() }
