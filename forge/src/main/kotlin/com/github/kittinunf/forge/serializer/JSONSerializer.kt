package com.github.kittinunf.forge.serializer

import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.Serializable

class StringSerializable : Serializable<String> {
    override fun serialize(model: String): JSON = JSON.String(model)
}

class NumberSerializable : Serializable<Number> {
    override fun serialize(model: Number): JSON = JSON.Number(model)
}

class BooleanSerializable : Serializable<Boolean> {
    override fun serialize(model: Boolean): JSON = JSON.Boolean(model)
}

fun String.toJson() = StringSerializable().serialize(this)

fun Number.toJson() = NumberSerializable().serialize(this)

fun Boolean.toJson() = BooleanSerializable().serialize(this)

fun <T> T.toJson(serializer: (T) -> JSON) =
        serializer(this)

fun <T> T?.maybeJson(serializer: (T) -> JSON) =
        if (this != null) {
            serializer(this)
        } else {
            JSON.Null()
        }

fun <T> Iterable<T>.toJson(serializer: T.() -> JSON) = JSON.Array(this.map(serializer))
