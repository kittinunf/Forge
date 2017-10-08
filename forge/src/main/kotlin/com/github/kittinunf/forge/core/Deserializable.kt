package com.github.kittinunf.forge.core

interface Deserializable<out T : Any> {

    val deserializer: (JSON) -> EncodedResult<T>

}