package com.github.kttinunf.forge.extension

import org.json.JSONArray

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public fun JSONArray.asSequence(): Sequence<Any> {
    return object : Sequence<Any> {

        override fun iterator() = object : Iterator<Any> {

            val it = (0..this@asSequence.length() - 1).iterator()

            override fun next(): Any {
                val i = it.next()
                return this@asSequence.get(i)
            }

            override fun hasNext() = it.hasNext()

        }
    }
}
 
