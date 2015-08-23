package com.github.kttinunf.forge.core

import org.json.JSONObject

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public class Forge {

    companion object {
        public inline fun <T, reified U: Deserializable<T>> json(json: String): T? = json<T, U>(JSONObject(json))

        public inline fun <T, reified U: Deserializable<T>> json(json: JSONObject): T? {
            val u = javaClass<U>().newInstance()
            return u.deserialize(JSON.parse(json))
        }
    }

}