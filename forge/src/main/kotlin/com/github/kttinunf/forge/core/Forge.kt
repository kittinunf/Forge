package com.github.kttinunf.forge.core

import org.json.JSONObject

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public class Forge {

    companion object {

        public fun <T, U : Deserializable<T>> fromJson(json: kotlin.String, deserializer: U): T? = fromJson(JSONObject(json), deserializer)

        public fun <T, U : Deserializable<T>> fromJson(json: JSONObject, deserializer: U): T? = deserializer.deserialize(JSON.parse(json))

    }

}