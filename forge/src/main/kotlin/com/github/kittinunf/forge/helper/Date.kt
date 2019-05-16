package com.github.kittinunf.forge.helper

import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.TypeMisMatchException
import java.text.SimpleDateFormat
import java.util.Date

fun toDate(style: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): (String) -> Date = {
    val formatter = SimpleDateFormat(style)
    formatter.parse(it)
}

fun deserializeDate(style: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", json: JSON): DeserializedResult<Date> = when (json) {
    is JSON.String -> {
        DeserializedResult.Success(toDate(style).invoke(json.value))
    }
    else -> DeserializedResult.Failure(TypeMisMatchException("String"))
}
