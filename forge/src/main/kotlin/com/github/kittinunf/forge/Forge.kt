package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.Deserializable
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.EncodedResult
import org.json.JSONArray
import org.json.JSONObject

object Forge {

    fun <T : Any, U : Deserializable<T>> modelFromJson(json: String, deserializer: U): EncodedResult<T> = deserializer.deserializer(JSON.parse(JSONObject(json)))

    fun <T : Any> modelFromJson(json: String, deserializer: JSON.() -> EncodedResult<T>): EncodedResult<T> = JSON.parse(JSONObject(json)).deserializer()

    fun <T : Any, U : Deserializable<T>> modelsFromJson(json: String, deserializer: U): List<EncodedResult<T>> =
            JSON.parse(JSONArray(json)).toList().map { deserializer.deserializer(it) }

    fun <T : Any> modelsFromJson(json: String, deserializer: JSON.() -> EncodedResult<T>): List<EncodedResult<T>> =
            JSON.parse(JSONArray(json)).toList().map { it.deserializer() }

}

