package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.DeserializedResult
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class DeserializedResultTest {

    @Test
    fun testGet() {
        val success = DeserializedResult.Success("foo")
        val failure = DeserializedResult.Failure<Any>(RuntimeException("error"))

        val value = success.get<String>()
        val error = failure.get<RuntimeException>()

        assertThat(value, equalTo("foo"))
        assertThat(error, instanceOf(RuntimeException::class.java))
        assertThat(error.message, equalTo("error"))
    }

    @Test
    fun testFoldSuccess() {
        val success = DeserializedResult.Success("foo")

        var successBlockWasCalled = false
        success.fold({
            successBlockWasCalled = true
        }, {})

        assertThat(successBlockWasCalled, equalTo(true))
    }

    @Test
    fun testFoldFailure() {
        val failure = DeserializedResult.Failure<Int>(RuntimeException("Error"))

        var failureBlockWasCalled = false
        failure.fold({}, {
            failureBlockWasCalled = true
        })

        assertThat(failureBlockWasCalled, equalTo(true))
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
        val failure = DeserializedResult.Failure<Int>(RuntimeException("error"))

        val mapped = failure.map { it * it }

        assertThat(mapped, instanceOf(DeserializedResult.Failure::class.java))
        assertThat(mapped.get<RuntimeException>(), instanceOf(RuntimeException::class.java))
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

        val fmapped = success.flatMap { DeserializedResult.Failure<Any>(IllegalStateException("illegal")) }

        assertThat(fmapped, instanceOf(DeserializedResult.Failure::class.java))

        val (v, e) = fmapped
        assertThat(v, nullValue())
        assertThat(e, instanceOf(IllegalStateException::class.java))
        assertThat(e?.message, equalTo("illegal"))
    }

    @Test
    fun testFlatMapOnFailureWithSuccess() {
        val failure = DeserializedResult.Failure<Int>(RuntimeException("error"))

        val fmapped = failure.flatMap { DeserializedResult.Success(1122) }

        assertThat(fmapped, instanceOf(DeserializedResult.Failure::class.java))

        val (v, e) = fmapped
        assertThat(v, nullValue())
        assertThat(e, instanceOf(RuntimeException::class.java))
        assertThat(e?.message, equalTo("error"))
    }

    @Test
    fun testFlatMapOnFailureWithFailure() {
        val failure = DeserializedResult.Failure<Int>(RuntimeException("error"))

        val fmapped = failure.flatMap { DeserializedResult.Failure<String>(IllegalStateException("another error")) }

        assertThat(fmapped, instanceOf(DeserializedResult.Failure::class.java))

        val (v, e) = fmapped
        assertThat(v, nullValue())
        assertThat(e, instanceOf(RuntimeException::class.java))
        assertThat(e?.message, equalTo("error"))
    }
}
