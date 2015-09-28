package com.github.kttinunf.forge.core

import com.github.kttinunf.forge.extension.asSequence
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

sealed public class JSON() : Sequence<JSON> {

    abstract val value: Any

    override fun iterator() = sequenceOf(this).iterator()

    public class Object(override val value: Map<kotlin.String, JSON> = mapOf()) : JSON() {

        override fun iterator() = object : Iterator<JSON> {

            val it = value.keySet().iterator()

            override fun next(): JSON {
                val key = it.next()
                return value[key]!!
            }

            override fun hasNext() = it.hasNext()

        }

    }

    public class Array(override val value: List<JSON> = listOf()) : JSON() {

        override fun iterator() = value.iterator()

    }

    public class String(override val value: kotlin.String = "") : JSON()

    public class Number(override val value: kotlin.Number = 0) : JSON()

    public class Boolean(override val value: kotlin.Boolean = false) : JSON()

    public class Null(override val value: Unit = Unit) : JSON()

    companion object {

        public fun parse(json: Any): JSON {
            when (json) {
                is JSONObject -> return parse(toMap(json))
                is JSONArray -> return parse(toList(json))

                is Map<*, *> -> return Object((json as Map<kotlin.String, Any>).asSequence().fold(hashMapOf<kotlin.String, JSON>()) { accum, entry ->
                    val (key, value) = entry
                    val jsonValue = parse(value)
                    accum += key to jsonValue
                    accum
                })

                is List<*> -> return Array(json.map { parse(it!!) })

                is kotlin.String -> return String(json)

                is kotlin.Number -> return Number(json)

                is kotlin.Boolean -> return Boolean(json)

                else -> return Null()
            }
        }

        private fun toMap(json: JSONObject): Map<kotlin.String, Any> {
            return if (json != JSONObject.NULL) {
                _toMap(json)
            } else emptyMap()
        }

        private fun toList(json: JSONArray): List<Any> {
            return if (json != JSONObject.NULL) {
                _toList(json)
            } else emptyList()
        }

        //recursive
        private fun _toMap(json: JSONObject): Map<kotlin.String, Any> {
            return json.asSequence().fold(hashMapOf<kotlin.String, Any>()) { accum, item ->
                val (key, value) = item
                val newValue: Any = if (value is JSONObject) {
                    _toMap(value)
                } else if (value is JSONArray) {
                    _toList(value)
                } else {
                    value
                }
                accum += (key to newValue)
                accum
            }
        }

        //recursive
        private fun _toList(json: JSONArray): List<Any> {
            return json.asSequence().fold(arrayListOf<Any>()) { accum, value ->
                val newValue: Any = if (value is JSONArray) {
                    _toList(value)
                } else if (value is JSONObject) {
                    _toMap(value)
                } else {
                    value
                }
                accum.add(newValue)
                accum
            }
        }

    }

    public fun <T : Any?> valueAs(): Result<T, Exception> {
        when (this) {
            is JSON.Null -> return Result.Success<T, Exception>(null)
            else -> {
                return (value as? T)?.
                        let { Result.Success<T, Exception>(it) } ?:
                        Result.Failure<T, Exception>(TypeMisMatchException(this.toString()))

            }
        }
    }

    public fun find(keyPath: kotlin.String): JSON? {
        val keys = keyPath.split(".")

        val initial: JSON? = this
        return keys.fold(initial) { json, key ->
            when (json) {
                is JSON.Object -> json.value.get(key)
                else -> null
            }
        }
    }

}
