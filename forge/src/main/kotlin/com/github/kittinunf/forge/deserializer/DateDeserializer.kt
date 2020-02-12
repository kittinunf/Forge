package com.github.kittinunf.forge.deserializer

import com.github.kittinunf.forge.core.AttributeTypeInvalidError
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.result.Result.Failure
import com.github.kittinunf.result.Result.Success
import java.text.SimpleDateFormat
import java.util.Date

fun toDate(style: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): (String) -> Date = {
    val formatter = SimpleDateFormat(style)
    formatter.parse(it)
}

fun JSON.deserializeDate(style: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): DeserializedResult<Date> =
        when (this) {
            is JSON.String -> {
                Success(toDate(style).invoke(value))
            }
            else -> Failure(AttributeTypeInvalidError("", javaClass, value))
        }
