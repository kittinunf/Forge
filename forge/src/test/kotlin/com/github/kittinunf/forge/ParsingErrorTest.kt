package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.AttributeTypeInvalidError
import com.github.kittinunf.forge.core.ForgeError
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.MissingAttributeError
import com.github.kittinunf.forge.core.apply
import com.github.kittinunf.forge.core.at
import com.github.kittinunf.forge.core.list
import com.github.kittinunf.forge.core.map
import com.github.kittinunf.forge.util.create
import com.github.kittinunf.result.Result.Failure
import com.github.kittinunf.result.Result.Success
import com.github.kittinunf.result.lift
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
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
        assertThat(user.error.message, equalTo("Attribute Type Invalid - key: \"age\", expected type: class java.lang.Double, received value: 46"))
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
        assertThat(user.error.message, equalTo("Attribute Type Invalid - key: \"levels\", expected type: class java.lang.Integer, received value: \"1\""))
    }

    @Test
    fun testSomeInvalidTypeItems() {
        val json = """
        [
            {
                "name": "Clementina DuBuque",
                "age": 46,
                "levels" : ["3","2","1"]
            },
            {
                "name": "DuBuque Clementina",
                "age": 64,
                "levels" : [3,2,1]
            }
        ]
        """.trimIndent()

        val result = Forge.modelsFromJson(json, User2.deserializer)
        val (users, error) = result

        assertThat((error as ForgeError), instanceOf(AttributeTypeInvalidError::class.java))
        assertThat((error as AttributeTypeInvalidError).expectedClass, instanceOf(Class::class.java))
        assertThat(error.message, equalTo("Attribute Type Invalid - key: \"levels\", expected type: class java.lang.Integer, received value: \"3\""))
        assertThat(users, nullValue())
    }

    @Test
    fun testSomeInvalidTypeItems() {
        val json = """
        [
            {
                "name": "Clementina DuBuque",
                "age": 46,
                "levels" : ["3","2","1"]
            },
            {
                "name": "DuBuque Clementina",
                "age": 64,
                "levels" : [3,2,1]
            }
        ]
        """.trimIndent()

        val users = Forge.modelsFromJson(json, User2.deserializer)
        val (user0, user1) = users

        assertThat((user0 as Failure).error, instanceOf(AttributeTypeInvalidError::class.java))
        assertThat(user0.error.message, equalTo("Attribute Type Invalid - key: \"levels\", expect type: class java.lang.Integer, received value: \"3\""))

        assertThat((user1 as Success).value, instanceOf(User2::class.java))
        assertThat(user1.value.age, equalTo(64))
        assertThat(user1.value.name, equalTo("DuBuque Clementina"))
        assertThat(user1.value.levels, hasItems(1,2,3))

        val liftedUser = users.lift()
        assertThat((liftedUser as Failure).error, instanceOf(AttributeTypeInvalidError::class.java))
        assertThat((liftedUser.error as AttributeTypeInvalidError).receivedValue as String, equalTo("3"))
        assertThat(liftedUser.error.message, equalTo("Attribute Type Invalid - key: \"levels\", expect type: class java.lang.Integer, received value: \"3\""))
    }
}
