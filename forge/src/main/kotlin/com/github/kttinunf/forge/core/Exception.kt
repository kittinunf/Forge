package com.github.kttinunf.forge.core

/**
 * Created by Kittinun Vantasin on 9/20/15.
 */

public class PropertyNotFoundException(val propertyName: String) : RuntimeException("Property name $propertyName is not found")

public class TypeMisMatchException(val expectedType: String) : RuntimeException("Type mismatch, expect type $expectedType")
