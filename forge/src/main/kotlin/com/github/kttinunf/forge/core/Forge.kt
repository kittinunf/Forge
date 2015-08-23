package com.github.kttinunf.forge.core

import org.json.JSONObject

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public class Forge {

    companion object {

        public inline fun <T, reified U : Deserializable<T>> fromJson(json: kotlin.String): T? = fromJson<T, U>(JSONObject(json))

        public inline fun <T, reified U : Deserializable<T>> fromJson(json: JSONObject): T? = javaClass<U>().newInstance().deserialize(JSON.parse(json))

    }

}