package com.github.kttinunf.forge.core

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public fun <T> JSON.at(key: kotlin.String, deserializer: JSON.() -> T): T? {
    return find(key)?.deserializer()
}

public fun <T> JSON.at(key: kotlin.String): T? = find(key)?.valueAs()

public fun <T, U> Function1<T, U>?.map(t: T?): U? {
    when(this) {
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
