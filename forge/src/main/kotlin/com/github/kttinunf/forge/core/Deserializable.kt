package com.github.kttinunf.forge.core

interface Deserializable<out T : Any> {

    val deserializer: (JSON) -> EncodedResult<T>

}
