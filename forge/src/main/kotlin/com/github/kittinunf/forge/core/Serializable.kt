package com.github.kittinunf.forge.core

interface Serializable<in T : Any> {

    fun serialize(model: T): JSON

}