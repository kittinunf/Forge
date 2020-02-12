package com.github.kittinunf.forge.core

import com.github.kittinunf.forge.deserializer.deserializeAs
import com.github.kittinunf.result.Result.Failure
import com.github.kittinunf.result.Result.Success
import com.github.kittinunf.result.lift
import com.github.kittinunf.result.map

infix fun <T, U> Function1<T, U>.map(result: DeserializedResult<T>) = result.map(this)

fun <T, U> DeserializedResult<(T) -> U>.apply(deserializedResult: DeserializedResult<T>): DeserializedResult<U> =
        when (this) {
            is Success -> deserializedResult.map(get())
            is Failure -> Failure(error)
        }

inline infix fun <reified T> JSON.at(key: String): DeserializedResult<T> = at(key, deserializer = { deserializeAs<T>(key) })

inline infix fun <reified T> JSON.maybeAt(key: String): DeserializedResult<T> = maybeAt(key, deserializer = { deserializeAs<T>(key) })

fun <T> JSON.at(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<T> =
        unfold(key, find(key)?.deserializer())
                ?: Failure(MissingAttributeError(key))

@Suppress("UNCHECKED_CAST")
fun <T> JSON.maybeAt(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<T> =
        unfold(key, find(key)?.deserializer()) ?: Success(null) as DeserializedResult<T>

inline infix fun <reified T> JSON.list(key: String) = list(key, deserializer = { deserializeAs<T>(key) })

inline infix fun <reified T> JSON.maybeList(key: String) = maybeList(key, deserializer = { deserializeAs<T>(key) })

fun <T> JSON.list(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<List<T>> =
        unfold(key, find(key)?.map(deserializer)?.toList()?.lift())
                ?: Failure(MissingAttributeError(key))

@Suppress("UNCHECKED_CAST")
fun <T> JSON.maybeList(key: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<List<T>> =
        unfold(key, find(key)?.map(deserializer)?.toList()?.lift()) ?: Success(null) as DeserializedResult<List<T>>

private fun <T> unfold(key: String, result: DeserializedResult<T>?): DeserializedResult<T>? =
        when (result) {
            is Success -> result
            is Failure -> if (result.error is AttributeTypeInvalidError) {
                val error = result.error as AttributeTypeInvalidError
                Failure(AttributeTypeInvalidError(key, error.expectedClass, error.receivedValue))
            } else result
            else -> null
        }
