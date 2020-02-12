package com.github.kittinunf.forge.deserializer

import com.github.kittinunf.forge.core.AttributeTypeInvalidError
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.JSON.Null
import com.github.kittinunf.result.Result.Failure
import com.github.kittinunf.result.Result.Success

<<<<<<< HEAD
inline fun <reified T : Any?> JSON.deserializeAs(key: String): DeserializedResult<T> = when (this) {
    is Null -> Success(null as T)
    else -> {
        (value as? T)?.let { Success(it) } ?: Failure(AttributeTypeInvalidError(key, T::class.java, value))
=======
inline fun <reified T : Any?> JSON.deserializeAs(key: String? = null): DeserializedResult<T> = when (this) {
    is Null -> Success(null as T)
    else -> {
        (value as? T)?.let(::Success) ?: Failure(AttributeTypeInvalidError(key ?: "(null)", T::class.java, value))
>>>>>>> 2aa55df... Fix inconsistencies
    }
}
