package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.AttributeTypeInvalidError
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.MissingAttributeError
import com.github.kittinunf.result.Result.Failure
import com.github.kittinunf.result.Result.Success
import com.github.kittinunf.result.flatMap
import com.github.kittinunf.result.map
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class DeserializedResultTest {

    @Test
    fun testGet() {
        val success = Success("foo")
        val failure = Failure(MissingAttributeError("bar"))

        val value = success.get()
        val error = failure.error

        assertThat(value, equalTo("foo"))
        assertThat(error, instanceOf(MissingAttributeError::class.java))
        assertThat(error.message, equalTo("Attribute is Missing - key \"bar\" is not found"))
    }

    @Test
    fun testMapSuccess() {
        val success = Success(10)

        val mapped = success.map { it * it }

        assertThat(mapped, instanceOf(Success::class.java))
        assertThat(mapped.get(), equalTo(100))
    }

    @Test
    fun testMapFailure() {
        val failure: DeserializedResult<Int> = Failure(AttributeTypeInvalidError("foo", String::class.java, 1))

        val mapped = failure.map { it * it }

        assertThat(mapped, instanceOf(Failure::class.java))
        assertThat((mapped as Failure).error, instanceOf(AttributeTypeInvalidError::class.java))
    }

    @Test
    fun testFlatMapOnSuccessWithSuccess() {
        val success = Success(listOf("hello world"))

        val fmapped = success.flatMap { Success(it.count()) }

        assertThat(fmapped, instanceOf(Success::class.java))

        val (v, e) = fmapped
        assertThat(v, equalTo(1))
        assertThat(e, nullValue())
    }

    @Test
    fun testFlatMapOnSuccessWithFailure() {
        val success = Success(listOf("hello world"))

        val fmapped = success.flatMap { Failure(MissingAttributeError("bar")) }

        assertThat(fmapped, instanceOf(Failure::class.java))

        val (v, e) = fmapped
        assertThat(v, nullValue())
        assertThat(e, instanceOf(MissingAttributeError::class.java))
        assertThat((e as MissingAttributeError).key, equalTo("bar"))
    }

    @Test
    fun testFlatMapOnFailureWithSuccess() {
        val failure = Failure(AttributeTypeInvalidError("foo", Int::class.java, "33"))

        val fmapped = failure.flatMap { Success(1122) }

        assertThat(fmapped, instanceOf(Failure::class.java))

        val (v, e) = fmapped
        assertThat(v, nullValue())
        assertThat(e, instanceOf(AttributeTypeInvalidError::class.java))
        assertThat((e as AttributeTypeInvalidError).key, equalTo("foo"))
        assertThat(e.receivedValue as String, equalTo("33"))
    }

    @Test
    fun testFlatMapOnFailureWithFailure() {
        val failure = Failure(MissingAttributeError("error"))

        val fmapped = failure.flatMap { Failure(MissingAttributeError("another error")) }

        assertThat(fmapped, instanceOf(Failure::class.java))

        val (v, e) = fmapped
        assertThat(v, nullValue())
        assertThat(e, instanceOf(MissingAttributeError::class.java))
        assertThat(e?.message, equalTo("Attribute is Missing - key \"error\" is not found"))
    }
}
