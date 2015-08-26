package com.github.kttinunf.forge

import com.github.kttinunf.forge.core.Deserializable
import com.github.kttinunf.forge.core.JSON
import org.json.JSONObject

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public class Forge {

    companion object {

        public fun <T, U : Deserializable<T>> fromJson(json: String, deserializer: U): T? = deserializer.deserializer(JSON.parse(JSONObject(json)))

        public fun <T> fromJson(json: String, deserializer: JSON.() -> T): T? = JSON.parse(JSONObject(json)).deserializer()

    }

}