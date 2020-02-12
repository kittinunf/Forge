package com.github.kittinunf.forge.extension

import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.result.Result.Success
import com.github.kittinunf.result.flatMap
import com.github.kittinunf.result.map

fun <U, T : DeserializedResult<U>> List<T>.lift(): DeserializedResult<List<U>> {
    return fold(Success(mutableListOf<U>()) as DeserializedResult<List<U>>) { acc, each ->
        acc.flatMap { arr ->
            each.map {
                (arr as MutableList).apply {
                    add(it)
                }
            }
        }
    }
}
