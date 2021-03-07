package com.github.kittinunf.forge.core

interface Serializable<T> {

    fun serialize(model: T): JSON
}
