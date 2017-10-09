package com.github.kittinunf.forge.function

import java.text.SimpleDateFormat
import java.util.*

fun toDate(style: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): (String) -> Date = {
    val formatter = SimpleDateFormat(style)
    formatter.parse(it)
}