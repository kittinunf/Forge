package com.github.kittinunf.forge.core

sealed class ForgeError(message: String) : Exception(message)

class MissingAttributeError(val key: String) : ForgeError("Attribute name \"$key\" is not found")

class AttributeTypeInvalidError(val key: String, val expectedClass: Class<*>, val receivedValue: Any) :
        ForgeError("Attribute Type Invalid, for key: $key, expect type: $expectedClass, received value: $receivedValue")
