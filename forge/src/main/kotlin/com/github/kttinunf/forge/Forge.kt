package com.github.kttinunf.forge

import com.github.kttinunf.forge.core.Deserializable
import com.github.kttinunf.forge.core.JSON
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public class Forge {

    companion object {

        public fun <T, U : Deserializable<T>> modelFromJson(json: String, deserializer: U): T? = deserializer.deserializer(JSON.parse(JSONObject(json)))

        public fun <T> modelFromJson(json: String, deserializer: JSON.() -> T): T? = JSON.parse(JSONObject(json)).deserializer()

        public fun <T, U : Deserializable<T>> modelsFromJson(json: String, deserializer: U): List<T?> =
                JSON.parse(JSONArray(json)).asSequence().map { modelFromJson(it, deserializer) }.toList()

        public fun <T> modelsFromJson(json: String, deserializer: JSON.() -> T): List<T?> =
                JSON.parse(JSONArray(json)).asSequence().map { modelFromJson(it, deserializer) }.toList()

        private fun <T> modelFromJson(json: JSON, deserializer: JSON.() -> T): T? = json.deserializer()

        private fun <T, U : Deserializable<T>> modelFromJson(json: JSON, deserializer: U): T? = deserializer.deserializer(json)
    }

}