package com.github.kttinunf.forge.core

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

//public fun <T : Deserializable<T>> JSON.at(keys: List<String>): T? = find(keys)?.resolve()

//public fun <T : Deserializable<T>> JSON.at(key: String): T? = find(key)?.resolve()

//helper
public inline fun <T, R> Iterable<T>.optFold(initial: R?, operation: (R?, T) -> R?): R? {
    var accumulator = initial
    for (element in this) accumulator = operation(accumulator, element)
    return accumulator
}
