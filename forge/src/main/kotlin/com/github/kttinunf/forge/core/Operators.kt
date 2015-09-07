package com.github.kttinunf.forge.core

import com.github.kttinunf.forge.Forge

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public fun <T> JSON.at(key: String, deserializer: JSON.() -> T): T? {
    val j = find(key)
    if (j != null) {
        return modelFromJson(j, deserializer)
    }
    return null
}

public fun <T> JSON.at(key: String): T? = at(key, { valueAs<T>() })

public fun <T> JSON.list(key: String, deserializer: JSON.() -> T): List<T?> {
    val j = find(key)
    if (j != null) {
        return j.asSequence().toList().map { it.deserializer() }
    }
    return emptyList()
}

public fun <T> JSON.list(key: String): List<T?> = list(key, { valueAs<T>() })

fun <T> modelFromJson(json: JSON, deserializer: JSON.() -> T): T? = json.deserializer()

fun <T, U : Deserializable<T>> modelFromJson(json: JSON, deserializer: U): T? = deserializer.deserializer(json)

public fun <T, U> Function1<T, U>?.map(t: T?): U? {
    when (this) {
        null -> return null
        else -> return _map(t)
    }
}

private fun <T, U> Function1<T, U>._map(t: T?): U? {
    when (t) {
        null -> return invoke(null)
        else -> return invoke(t)
    }
}
