package com.github.kittinunf.forge.core

sealed class ForgeError(message: String) : Exception(message)

class MissingAttributeError(val key: String) : ForgeError("Attribute is Missing - key \"$key\" is not found")

class AttributeTypeInvalidError(val key: String, val expectedClass: Class<*>, val receivedValue: Any) :
        ForgeError("Attribute Type Invalid - key: \"$key\", expected type: $expectedClass, received value: ${if (receivedValue is String) "\"$receivedValue\"" else receivedValue}")
