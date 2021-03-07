package com.github.kittinunf.forge.stringifier

import com.github.kittinunf.forge.core.JSON

fun JSON.asString(): String =
        when (this) {
            is JSON.Object -> {

                val stringBuilder = StringBuilder()
                stringBuilder.append("{")

                var first = true
                value.forEach { (key, value) ->
                    if (first) first = false else stringBuilder.append(",")

                    stringBuilder.append("\"$key\":")
                    stringBuilder.append(value.asString())
                }

                stringBuilder.append("}")
                stringBuilder.toString()
            }

            is JSON.Array -> {

                val stringBuilder = StringBuilder()
                stringBuilder.append("[")

                var first = true
                value.forEach { entry ->
                    if (first) first = false else stringBuilder.append(",")

                    stringBuilder.append(entry.asString())
                }

                stringBuilder.append("]")
                stringBuilder.toString()
            }

            is JSON.String -> "\"$value\""

            is JSON.Number -> value.toString()

            is JSON.Boolean -> value.toString()

            is JSON.Null -> "null"
        }
