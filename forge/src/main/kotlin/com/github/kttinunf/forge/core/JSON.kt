package com.github.kttinunf.forge.core

import com.github.kttinunf.forge.extension.asSequence
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public abstract class JSON(val value: Any) {

    companion object Type {

        public class Object(value: Map<kotlin.String, JSON> = mapOf()) : JSON(value)

        public class Array(value: List<JSON> = listOf()) : JSON(value)

        public class String(value: kotlin.String = "") : JSON(value)

        public class Number(value: kotlin.Number = 0) : JSON(value)

        public class Boolean(value: kotlin.Boolean = false) : JSON(value)

        public class NULL(value: Unit = Unit) : JSON(value)

        public fun parse(json: Any): JSON {
            when (json) {
                is JSONObject -> return parse(toMap(json))

                is Map<*, *> -> return Object((json as Map<kotlin.String, Any>).asSequence().fold(hashMapOf<kotlin.String, JSON>()) { aggregator, entry ->
                    val (key, value) = entry
                    val jsonValue = parse(value)
                    aggregator += key to jsonValue
                    aggregator
                })

                is List<*> -> return Array(json.map { parse(it!!) })

                is kotlin.String -> return String(json)

                is kotlin.Number -> return Number(json)

                is kotlin.Boolean -> return Boolean(json)

                else -> return NULL()
            }
        }

        private fun toMap(json: JSONObject): Map<kotlin.String, Any> {
            return if (json != JSONObject.NULL) {
                _toMap(json)
            } else emptyMap()
        }

        //recursive
        private fun _toMap(json: JSONObject): Map<kotlin.String, Any> {
            return json.asSequence().fold(hashMapOf<kotlin.String, Any>()) { aggregator, item ->
                val (key, value) = item
                val newValue: Any = if (value is JSONObject) {
                    _toMap(value)
                } else if (value is JSONArray) {
                    _toList(value)
                } else {
                    value
                }
                aggregator += (key to newValue)
                aggregator
            }
        }

        private fun _toList(json: JSONArray): List<Any> {
            return json.asSequence().fold(arrayListOf<Any>()) { aggregator, value ->
                val newValue: Any = if (value is JSONArray) {
                    _toList(value)
                } else if (value is JSONObject) {
                    _toMap(value)
                } else {
                    value
                }
                aggregator.add(newValue)
                aggregator
            }
        }

    }

    public fun <T> valueAs(): T? {
        when (this) {
            is JSON.Type.NULL -> return null
            else -> return value as T
        }
    }

    private fun get(key: kotlin.String): JSON? {
        when (this) {
            is JSON.Type.Object -> return (value as Map<kotlin.String, JSON?>)[key]
            else -> return null
        }
    }

    public fun find(keyPath: kotlin.String): JSON? {
        val keys = keyPath.splitBy(".")

        val initial: JSON? = this
        return keys.fold(initial) { json, key ->
            json?.get(key)
        }
    }

}





