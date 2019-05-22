package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.AttributeTypeInvalidError
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.MissingAttributeError
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class DeserializedResultTest {

    @Test
    fun testGet() {
        val success = DeserializedResult.Success("foo")
        val failure = DeserializedResult.Failure<Any>(MissingAttributeError("bar"))

        val value = success.get<String>()
        val error = failure.error()

        assertThat(value, equalTo("foo"))
        assertThat(error, instanceOf(MissingAttributeError::class.java))
        assertThat(error.message, equalTo("Attribute is Missing - key \"bar\" is not found"))
    }

    @Test
    fun testMapSuccess() {
        val success = DeserializedResult.Success(10)

        val mapped = success.map { it * it }

        assertThat(mapped, instanceOf(DeserializedResult.Success::class.java))
        assertThat(mapped.get<Int>(), equalTo(100))
    }

    @Test
    fun testMapFailure() {
        val failure = DeserializedResult.Failure<Int>(AttributeTypeInvalidError("foo", String::class.java, 1))

        val mapped = failure.map { it * it }

        assertThat(mapped, instanceOf(DeserializedResult.Failure::class.java))
        assertThat(mapped.error(), instanceOf(AttributeTypeInvalidError::class.java))
    }

    @Test
    fun testFlatMapOnSuccessWithSuccess() {
        val success = DeserializedResult.Success(listOf("hello world"))

        val fmapped = success.flatMap { DeserializedResult.Success(it.count()) }

        assertThat(fmapped, instanceOf(DeserializedResult.Success::class.java))

        val (v, e) = fmapped
        assertThat(v, equalTo(1))
        assertThat(e, nullValue())
    }

    @Test
    fun testFlatMapOnSuccessWithFailure() {
        val success = DeserializedResult.Success(listOf("hello world"))

        val fmapped = success.flatMap { DeserializedResult.Failure<Any>(MissingAttributeError("bar")) }

        assertThat(fmapped, instanceOf(DeserializedResult.Failure::class.java))

        val (v, e) = fmapped
        assertThat(v, nullValue())
        assertThat(e, instanceOf(MissingAttributeError::class.java))
        assertThat((e as MissingAttributeError).key, equalTo("bar"))
    }

    @Test
    fun testFlatMapOnFailureWithSuccess() {
        val failure = DeserializedResult.Failure<Int>(AttributeTypeInvalidError("foo", Int::class.java, "33"))

        val fmapped = failure.flatMap { DeserializedResult.Success(1122) }

        assertThat(fmapped, instanceOf(DeserializedResult.Failure::class.java))

        val (v, e) = fmapped
        assertThat(v, nullValue())
        assertThat(e, instanceOf(AttributeTypeInvalidError::class.java))
        assertThat((e as AttributeTypeInvalidError).key, equalTo("foo"))
        assertThat(e.receivedValue as String, equalTo("33"))
    }

    @Test
    fun testFlatMapOnFailureWithFailure() {
        val failure = DeserializedResult.Failure<Int>(MissingAttributeError("error"))

        val fmapped = failure.flatMap { DeserializedResult.Failure<String>(MissingAttributeError("another error")) }

        assertThat(fmapped, instanceOf(DeserializedResult.Failure::class.java))

        val (v, e) = fmapped
        assertThat(v, nullValue())
        assertThat(e, instanceOf(MissingAttributeError::class.java))
        assertThat(e?.message, equalTo("Attribute is Missing - key \"error\" is not found"))
    }
}
