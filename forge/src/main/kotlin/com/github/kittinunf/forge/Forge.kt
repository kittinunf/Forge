package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.Deserializable
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.Serializable
import com.github.kittinunf.forge.extension.lift
import org.json.JSONArray
import org.json.JSONObject

object Forge {

    fun <T : Any, U : Deserializable<T>> modelFromJson(json: String, deserializer: U): DeserializedResult<T> =
            deserializer.deserialize(JSON.parse(JSONObject(json)))

    fun <T : Any> modelFromJson(json: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<T> =
            JSON.parse(JSONObject(json)).deserializer()

    fun <T : Any, U : Deserializable<T>> modelsFromJson(json: String, deserializer: U): DeserializedResult<List<T>> =
            JSON.parse(JSONArray(json))
                    .map(deserializer::deserialize)
                    .toList()
                    .lift()

    fun <T : Any> modelsFromJson(json: String, deserializer: JSON.() -> DeserializedResult<T>): DeserializedResult<List<T>> =
            JSON.parse(JSONArray(json))
                    .map(deserializer)
                    .toList()
                    .lift()

    fun <T : Any> jsonFromModel(model: T, serializer: T.() -> JSON): JSON =
            model.serializer()

    fun <T : Any, U : Serializable<T>> jsonFromModel(model: T, serializer: U): JSON =
            serializer.serialize(model)

    fun <T : Any> jsonFromModels(models: List<T>, serializer: T.() -> JSON): JSON =
            JSON.Array(models.map(serializer))

    fun <T : Any, U : Serializable<T>> jsonFromModels(models: List<T>, serializable: U): JSON =
            JSON.Array(models.map(serializable::serialize))

}