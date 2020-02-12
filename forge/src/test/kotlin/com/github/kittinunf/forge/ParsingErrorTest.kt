package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.AttributeTypeInvalidError
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.MissingAttributeError
import com.github.kittinunf.forge.core.apply
import com.github.kittinunf.forge.core.at
import com.github.kittinunf.forge.core.list
import com.github.kittinunf.forge.core.map
import com.github.kittinunf.forge.util.create
import com.github.kittinunf.result.Result.Failure
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ParsingErrorTest : BaseTest() {

    data class User1(val id: Int, val name: String, val age: Double) {
        companion object {
            val deserializer = { j: JSON ->
                ::User1.create
                        .map(j at "id")
                        .apply(j at "name")
                        .apply(j at "age")
            }
        }
    }

    data class User2(val name: String, val age: Int, val levels: List<Int>) {
        companion object {
            val deserializer = { j: JSON ->
                ::User2.create
                        .map(j at "name")
                        .apply(j at "age")
                        .apply(j list "levels")
            }
        }
    }

    @Test
    fun testMissingItem() {
        val json = """
        {
          "name": "Clementina DuBuque",
          "age": 46,
        }
        """.trimIndent()

        val user = Forge.modelFromJson(json, User1.deserializer)

        assertThat((user as Failure).error, instanceOf(MissingAttributeError::class.java))
        assertThat(user.error.message, equalTo("Attribute is Missing - key \"id\" is not found"))
    }

    @Test
    fun testInvalidTypeItem() {
        val json = """
        {
          "id": 1,
          "name": "Clementina DuBuque",
          "age": 46
        }
        """.trimIndent()

        val user = Forge.modelFromJson(json, User1.deserializer)

        assertThat((user as Failure).error, instanceOf(AttributeTypeInvalidError::class.java))
        assertThat(user.error.message, equalTo("Attribute Type Invalid - key: \"age\", expect type: class java.lang.Double, received value: 46"))
    }

    @Test
    fun testMissingItems() {
        val json = """
        {
          "name": "Clementina DuBuque",
          "age": 46
        }
        """.trimIndent()

        val user = Forge.modelFromJson(json, User2.deserializer)

        assertThat((user as Failure).error, instanceOf(MissingAttributeError::class.java))
        assertThat(user.error.message, equalTo("Attribute is Missing - key \"levels\" is not found"))
    }

    @Test
    fun testInvalidTypeItems() {
        val json = """
        {
          "name": "Clementina DuBuque",
          "age": 46,
          "levels" : ["1","2","3"]
        }
        """.trimIndent()

        val user = Forge.modelFromJson(json, User2.deserializer)

        assertThat((user as Failure).error, instanceOf(AttributeTypeInvalidError::class.java))
        assertThat(user.error.message, equalTo("Attribute Type Invalid - key: \"levels\", expect type: class java.lang.Integer, received value: \"1\""))
    }
}
