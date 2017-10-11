package com.github.kittinunf.forge.core

interface Deserializable<out T : Any> {

    fun deserialize(json: JSON): DeserializedResult<T>

}