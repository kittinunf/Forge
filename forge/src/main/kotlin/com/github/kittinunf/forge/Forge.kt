package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.Deserializable
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.result.lift
import org.json.JSONArray
import org.json.JSONObject

object Forge {

    fun <T : Any, U : Deserializable<T>> modelFromJson(json: String, deserializer: U): DeserializedResult<T> =
        deserializer.deserialize(JSON.parse(JSONObject(json)))

    fun <T : Any> modelFromJson(json: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<T> =
        JSON.parse(JSONObject(json)).deserializer()

    fun <T : Any, U : Deserializable<T>> modelsFromJson(json: String, deserializer: U): DeserializedResult<List<T>> =
        JSON.parse(JSONArray(json)).toList().map(deserializer::deserialize).lift()

    fun <T : Any> modelsFromJson(json: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<List<T>> =
        JSON.parse(JSONArray(json)).toList().map(deserializer).lift()
}
