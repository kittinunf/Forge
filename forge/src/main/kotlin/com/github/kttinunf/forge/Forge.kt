package com.github.kttinunf.forge

import com.github.kttinunf.forge.core.*
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

object Forge {

    public fun <T: Any, U : Deserializable<T>> modelFromJson(json: String, deserializer: U): Result<T> = deserializer.deserializer(JSON.parse(JSONObject(json)))

    public fun <T: Any> modelFromJson(json: String, deserializer: JSON.() -> Result<T>): Result<T> = JSON.parse(JSONObject(json)).deserializer()

    public fun <T: Any, U : Deserializable<T>> modelsFromJson(json: String, deserializer: U): List<Result<T>> =
            JSON.parse(JSONArray(json)).toList().map { deserializer.deserializer(it) }

    public fun <T: Any> modelsFromJson(json: String, deserializer: JSON.() -> Result<T>): List<Result<T>> =
            JSON.parse(JSONArray(json)).toList().map { it.deserializer() }

}

