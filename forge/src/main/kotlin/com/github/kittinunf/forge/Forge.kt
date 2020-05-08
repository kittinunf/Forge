package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.*
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

    fun <T : Any, U : Serializable<T>> jsonFromModel(model: T, serializer: U): JSON =
            serializer.serialize(model)

    fun <T : Any> jsonFromModel(model: T, serializer: T.() -> JSON): JSON =
            model.serializer()
}
