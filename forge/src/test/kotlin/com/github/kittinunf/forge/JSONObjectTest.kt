package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.MissingAttributeError
import com.github.kittinunf.forge.deserializer.deserializeAs
import com.github.kittinunf.result.Result.Failure
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.json.JSONObject
import org.junit.Test

class JSONObjectTest : BaseTest() {

    @Test
    fun testParseJSON1() {
        val j = JSONObject("{ \"hello\" : \"world\" }")
        val json = JSON.parse(j)
        assertThat(json.value, notNullValue())
    }

    @Test
    fun testParseJSON2() {
        val j = JSONObject(userJson)
        val json = JSON.parse(j)
        assertThat(json.value, notNullValue())
    }

    @Test
    fun testJSONValidValue() {
        val json = JSON.parse((JSONObject(userJson)))

        val id = json.find("id")?.deserializeAs<Int>()
        assertThat(id, notNullValue())
        assertThat(id!!.get(), equalTo(1))

        val name = json.find("name")?.deserializeAs<String>()

        assertThat(name, notNullValue())
        assertThat(name!!.get(), equalTo("Clementina DuBuque"))

        val isDeleted = json.find("is_deleted")?.deserializeAs<Boolean>()

        assertThat(isDeleted, notNullValue())
        assertThat(isDeleted!!.get(), equalTo(true))

        val addressStreet = json.find("address.street")?.deserializeAs<String>()

        assertThat(addressStreet, notNullValue())
        assertThat(addressStreet!!.get(), equalTo("Kattie Turnpike"))

        val addressGeoLat = json.find("address.geo.lat")?.deserializeAs<Double>()

        assertThat(addressGeoLat, notNullValue())
        assertThat(addressGeoLat!!.get(), equalTo(-38.2386))
    }

    @Test
    fun testJSONInvalidValue() {
        val json = JSON.parse((JSONObject(userJson)))

        val notFoundName = json.find("n")?.deserializeAs<String>()
            ?: Failure(MissingAttributeError("n"))

        assertThat(notFoundName, notNullValue())
        assertThat((notFoundName as Failure).error, instanceOf(MissingAttributeError::class.java))

        val notFoundAddressSt = json.find("address.st")?.deserializeAs<String>()
            ?: Failure(MissingAttributeError("address.st"))

        assertThat(notFoundAddressSt, notNullValue())
        assertThat((notFoundAddressSt as Failure).error, instanceOf(MissingAttributeError::class.java))
    }
}
