package com.github.kttinunf.forge.extension

import org.json.JSONObject
import kotlin.reflect.jvm.kotlin

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public fun JSONObject.asSequence(): Sequence<Pair<String, Any>> {
    return object : Sequence<Pair<String, Any>> {

        override fun iterator() = object : Iterator<Pair<String, Any>> {

            val iter = keys()

            override fun next(): Pair<String, Any> {
                val key = iter.next()
                val value = get(key)
                return (key to value)
            }

            override fun hasNext() = iter.hasNext()

        }

    }
}
