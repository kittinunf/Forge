package com.github.kittinunf.forge.deserializer

import com.github.kittinunf.forge.core.AttributeTypeInvalidError
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.JSON.Null

inline fun <reified T : Any?> JSON.deserializeAs(): DeserializedResult<T> = when (this) {
    is Null -> DeserializedResult.Success<T>(null)
    else -> {
        (value as? T)?.let { DeserializedResult.Success(it) }
                ?: DeserializedResult.Failure(AttributeTypeInvalidError("", T::class.java, value))
    }
}
