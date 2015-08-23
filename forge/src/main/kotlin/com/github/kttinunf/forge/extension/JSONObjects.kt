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

public inline fun <reified T> JSONObject.get(key: String): T {
    val keys = linkedListOf(*key.splitBy(".").toTypedArray())

    var json = this

    while (keys.size() != 1) {
        json = json.getJSONObject(keys.remove())
    }

    val key = keys[0]

    val clazz = javaClass<T>().kotlin
    when (clazz.simpleName) {
        "Boolean" -> return json.getBoolean(key) as T
        "Double" -> return json.getDouble(key) as T
        "Int" -> return json.getInt(key) as T
        "JSONArray" -> return json.getJSONArray(key) as T
        "JSONObject" -> return json.getJSONObject(key) as T
        "Long" -> return json.getLong(key) as T
        "String" -> return json.getString(key) as T
        else -> throw UnsupportedOperationException()
    }
}

