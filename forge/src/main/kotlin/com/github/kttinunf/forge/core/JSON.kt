package com.github.kttinunf.forge.core

import com.github.kttinunf.forge.extension.asSequence
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public abstract class JSON(val value: Any) : Sequence<JSON> {

    override fun iterator() = sequenceOf(this).iterator()

    companion object Type {

        public class Object(value: Map<kotlin.String, JSON> = mapOf()) : JSON(value) {

            override fun iterator() = object : Iterator<JSON> {

                val map = value as Map<kotlin.String, JSON>
                val it = map.keySet().iterator()

                override fun next(): JSON {
                    val key = it.next()
                    return map[key]!!
                }

                override fun hasNext() = it.hasNext()

            }

        }

        public class Array(value: List<JSON> = listOf()) : JSON(value) {

            override fun iterator() = (value as List<JSON>).iterator()

        }

        public class String(value: kotlin.String = "") : JSON(value)

        public class Number(value: kotlin.Number = 0) : JSON(value)

        public class Boolean(value: kotlin.Boolean = false) : JSON(value)

        public class Null(value: Unit = Unit) : JSON(value)

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
            is JSON.Type.Null -> return Result.Success<T, Exception>(null)
            else -> {
                return (value as? T).unfold({
                    Result.Success<T, Exception>(it)
                }, {
                    Result.Failure<T, Exception>(TypeMisMatchException(this.toString()))
                })
            }
        }
    }

    private fun get(key: kotlin.String): JSON? {
        when (this) {
            is JSON.Type.Object -> return (value as Map<kotlin.String, JSON?>)[key]
            else -> return null
        }
    }

    public fun find(keyPath: kotlin.String): JSON? {
        val keys = keyPath.split(".")

        val initial: JSON? = this
        return keys.fold(initial) { json, key ->
            json?.get(key)
        }
    }

}
