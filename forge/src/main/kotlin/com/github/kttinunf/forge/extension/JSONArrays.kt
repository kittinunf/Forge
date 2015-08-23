package com.github.kttinunf.forge.extension

import org.json.JSONArray

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

fun JSONArray.asSequence(): Sequence<Any> {
    return object : Sequence<Any> {

        override fun iterator() = object : Iterator<Any> {

            val range = (0..this@asSequence.length())

            override fun next(): Any {
                val i = range.iterator().next()
                return this@asSequence.get(i)
            }

            override fun hasNext() = range.iterator().hasNext()

        }
    }
}
 
