package com.github.kttinunf.forge.core

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public fun <T, U : Deserializable<T>> JSON.at(key: kotlin.String, deserializer: U): T {
    return deserializer.deserialize(find(key)!!)!!
}

public fun <T> JSON.at(key: kotlin.String): T = find(key)!!.valueAs()!!

public fun <T> JSON.maybe(key: kotlin.String): T? = find(key)?.valueAs()

//helper
public inline fun <T, R> Iterable<T>.optFold(initial: R?, operation: (R?, T) -> R?): R? {
    var accumulator = initial
    for (element in this) accumulator = operation(accumulator, element)
    return accumulator
}

public fun <T, U> Function1<T, U>.map(t: T?): U? {
    when (t) {
        null -> return null
        else -> return invoke(t)
    }
}

public fun <T, U> Function1<T, U>?.apply(t: T?): U? {
    when (this) {
        null -> return null
        else -> return map(t)
    }
}
