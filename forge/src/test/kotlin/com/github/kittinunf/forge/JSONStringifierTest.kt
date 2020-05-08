package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.stringifier.asString
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class JSONStringifierTest {

    @Test
    fun testJSONNullStringifier() = testStringifier(
            json = JSON.Null(),
            expectedString = "null"
    )

    @Test
    fun testJSONBooleanStringifier() = testStringifier(
            json = JSON.Boolean(true),
            expectedString = "true"
    )

    @Test
    fun testJSONNumberStringifier() = testStringifier(
            json = JSON.Number(-42.41),
            expectedString = "-42.41"
    )

    @Test
    fun testJSONStringEmptyStringifier() = testStringifier(
            json = JSON.String(""),
            expectedString = "\"\""
    )

    @Test
    fun testJSONStringStringifier() = testStringifier(
            json = JSON.String("stringify"),
            expectedString = "\"stringify\""
    )

    @Test
    fun testJSONEmptyArrayStringifier() = testStringifier(
            json = JSON.Array(),
            expectedString = "[]"
    )

    @Test
    fun testJSONArraySimpleStringifier() = testStringifier(
            json = JSON.Array(listOf(
                    JSON.String("string"),
                    JSON.Number(42.41),
                    JSON.Null()
            )),
            expectedString = "[\"string\",42.41,null]"
    )

    @Test
    fun testJSONArrayNestedStringifier() = testStringifier(
            json = JSON.Array(listOf(
                    JSON.String("string"),
                    JSON.Array(listOf(
                            JSON.Number(42),
                            JSON.Array(listOf(
                                    JSON.Null()
                            ))
                    ))
            )),
            expectedString = "[\"string\",[42,[null]]]"
    )

    @Test
    fun testJSONObjectEmptyStringifier() = testStringifier(
            json = JSON.Object(),
            expectedString = "{}"
    )

    @Test
    fun testJSONObjectSimpleStringifier() = testStringifier(
            json = JSON.Object(mapOf(
                    "one" to JSON.String("string"),
                    "two" to JSON.Number(-13.21),
                    "three" to JSON.Boolean(false)
            )),
            expectedString = "{\"one\":\"string\",\"two\":-13.21,\"three\":false}"
    )

    @Test
    fun testJSONObjectNestedStringifier() = testStringifier(
            json = JSON.Object(mapOf(
                    "one" to JSON.Object(mapOf(
                            "two" to JSON.Array(listOf(
                                    JSON.Object(mapOf(
                                            "three" to JSON.Boolean(false)
                                    ))
                            ))
                    ))
            )),
            expectedString = "{\"one\":{\"two\":[{\"three\":false}]}}"
    )

    private fun testStringifier(json: JSON, expectedString: String) =
            assertThat(json.asString(), equalTo(expectedString))
}