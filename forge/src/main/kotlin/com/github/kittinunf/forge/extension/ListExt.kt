package com.github.kittinunf.forge.extension

import com.github.kittinunf.forge.core.DeserializedResult

fun <U, T : DeserializedResult<U>> List<T>.lift(): DeserializedResult<List<U>> {
    return fold(DeserializedResult.Success(mutableListOf<U>()) as DeserializedResult<List<U>>) { acc, each ->
        acc.flatMap { arr ->
            each.map {
                (arr as MutableList).apply {
                    add(it)
                }
            }
        }
    }
}
