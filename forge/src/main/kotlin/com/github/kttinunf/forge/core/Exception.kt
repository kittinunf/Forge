package com.github.kttinunf.forge.core

class PropertyNotFoundException(val propertyName: String) : RuntimeException("Property name $propertyName is not found")

class TypeMisMatchException(val expectedType: String) : RuntimeException("Type mismatch, expect type $expectedType")
