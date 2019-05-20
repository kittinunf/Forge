package com.github.kittinunf.forge.core

import com.github.kittinunf.forge.core.DeserializedResult.Failure
import com.github.kittinunf.forge.core.DeserializedResult.Success
import com.github.kittinunf.forge.extension.lift

infix fun <T, U> Function1<T, U>.map(deserializedResult: DeserializedResult<T>) = deserializedResult.map(this)

fun <T, U> DeserializedResult<(T) -> U>.apply(deserializedResult: DeserializedResult<T>): DeserializedResult<U> =
        when (this) {
            is Success -> deserializedResult.map(get())
            is Failure -> Failure(error())
        }

fun <T> JSON.at(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<T> {
    return find(key)?.deserializer() ?: Failure(MissingAttributeError(key))
}

fun <T> JSON.maybeAt(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<T> {
    return find(key)?.deserializer() ?: Success(null)
}

inline infix fun <reified T> JSON.at(key: String): DeserializedResult<T> = at(key, deserializer = { valueAs<T>(key) })

inline infix fun <reified T> JSON.maybeAt(key: String): DeserializedResult<T> = maybeAt(key, deserializer = { valueAs<T>(key) })

fun <T> JSON.list(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<List<T>> {
    return find(key)?.map(deserializer)
            ?.toList()
            ?.lift()
            ?: Failure(MissingAttributeError(key))
}

fun <T> JSON.maybeList(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<List<T>> {
    return find(key)?.map(deserializer)
            ?.toList()
            ?.lift()
            ?: Success(null)
}

inline infix fun <reified T> JSON.list(key: String) = list(key, deserializer = { valueAs<T>(key) })

inline infix fun <reified T> JSON.maybeList(key: String) = maybeList(key, deserializer = { valueAs<T>(key) })
