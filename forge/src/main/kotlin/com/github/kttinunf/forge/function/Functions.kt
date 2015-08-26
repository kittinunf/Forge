package com.github.kttinunf.forge.function

import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by Kittinun Vantasin on 8/26/15.
 */

//date
public fun toDate(style: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): (String) -> Date = {
    val formatter = SimpleDateFormat(style)
    formatter.parse(it)
}
