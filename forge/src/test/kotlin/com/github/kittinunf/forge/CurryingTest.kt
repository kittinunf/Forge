package com.github.kittinunf.forge

import com.github.kittinunf.forge.JSONMappingObjectTest.FriendDeserializer
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.at
import com.github.kittinunf.forge.core.maybeAt
import com.github.kittinunf.forge.model.Friend
import com.github.kittinunf.forge.model.User
import com.github.kittinunf.forge.model.UserWithOptionalFields
import com.github.kittinunf.forge.util.create
import com.github.kittinunf.forge.util.curry
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.json.JSONObject
import org.junit.Test

class CurryingTest : BaseTest() {

    @Test
    fun testUserModelCurry() {
        val json = JSON.parse((JSONObject(userJson)))

        val curry = ::User.curry()

        val id: DeserializedResult<Int> = (json at "id")
        val username: DeserializedResult<String> = (json at "username")
        val name: DeserializedResult<String> = (json at "name")
        val age: DeserializedResult<Int> = (json at "age")
        val email: DeserializedResult<String> = (json at "email")
        val levels: DeserializedResult<List<Int>> = (json at "levels")
        val fr: DeserializedResult<Friend> = (json.at("friend", FriendDeserializer()::deserialize))

        val user = curry(id.get())(username.get())(name.get())(age.get())(email.get())(levels.get())(fr.get())

        assertThat(user.id, equalTo(1))
        assertThat(user.name, equalTo("Clementina DuBuque"))
        assertThat(user.age, equalTo(46))
        assertThat(user.email, equalTo("Rey.Padberg@karina.biz"))
    }

    @Test
    fun testUserModelOptionalCurry() {
        val json = JSON.parse((JSONObject(userJson)))

        val curry = ::UserWithOptionalFields.curry()

        val name: DeserializedResult<String> = (json at "name")
        val phone: DeserializedResult<String> = (json at "phone")
        val weight: DeserializedResult<Double> = (json at "weight")
        val city: DeserializedResult<String> = (json maybeAt "city")
        val gender: DeserializedResult<String> = (json maybeAt "gender")

        val user = curry(name.get())(city.get())(gender.get())(phone.get())(weight.get())

        assertThat(user.name, equalTo("Clementina DuBuque"))
        assertThat(user.phone, equalTo("024-648-3804"))
        assertThat(user.weight, equalTo(72.5))
        assertThat(user.city, nullValue())
        assertThat(user.gender, nullValue())
    }

    @Test
    fun testCurrying1() {
        val negate = { x: Int -> x * (-1) }
        val curry = negate.create
        assertThat(curry(1), equalTo(-1))
    }

    @Test
    fun testCurrying2() {
        val multiply = { x: Int, y: Int -> x * y }
        val curry = multiply.create
        assertThat(curry(2)(3), equalTo(6))
    }

    @Test
    fun testCurrying3() {
        val concat = { x: String, y: String, z: String -> x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc"), equalTo("abbccc"))
    }

    @Test
    fun testCurrying4() {
        val multiply = { w: Int, x: Int, y: Int, z: Int -> w * x * y * z }
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4), equalTo(24))
    }

    @Test
    fun testCurrying5() {
        val concat = { v: String, w: String, x: String, y: String, z: String -> v + w + x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc")("dddd")("eeeee"), equalTo("abbcccddddeeeee"))
    }

    @Test
    fun testCurrying6() {
        val multiply = { u: Int, v: Int, w: Int, x: Int, y: Int, z: Int -> u * v * w * x * y * z }
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4)(5)(6), equalTo(720))
    }

    @Test
    fun testCurrying7() {
        val concat = { t: String, u: String, v: String, w: String, x: String, y: String, z: String -> t + u + v + w + x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc")("dddd")("eeeee")("f")("gg"), equalTo("abbcccddddeeeeefgg"))
    }

    @Test
    fun testCurrying8() {
        val multiply = { s: Int, t: Int, u: Int, v: Int, w: Int, x: Int, y: Int, z: Int -> s * t * u * v * w * x * y * z }
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4)(5)(6)(7)(8), equalTo(40_320))
    }

    @Test
    fun testCurrying9() {
        val concat = { r: String, s: String, t: String, u: String, v: String, w: String, x: String, y: String, z: String -> r + s + t + u + v + w + x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc")("dddd")("eeeee")("f")("gg")("hhh")("iiii"), equalTo("abbcccddddeeeeefgghhhiiii"))
    }

    @Test
    fun testCurrying10() {
        val multiply = { q: Int, r: Int, s: Int, t: Int, u: Int, v: Int, w: Int, x: Int, y: Int, z: Int -> q * r * s * t * u * v * w * x * y * z }
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4)(5)(6)(7)(8)(9)(10), equalTo(3_628_800))
    }

    @Test
    fun testCurrying11() {
        val concat = { p: String, q: String, r: String, s: String, t: String, u: String, v: String, w: String, x: String, y: String, z: String -> p + q + r + s + t + u + v + w + x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc")("dddd")("eeeee")("f")("gg")("hhh")("iiii")("jjjjj")("k"), equalTo("abbcccddddeeeeefgghhhiiiijjjjjk"))
    }

    @Test
    fun testCurrying12() {
        val multiply = { o: Int, p: Int, q: Int, r: Int, s: Int, t: Int, u: Int, v: Int, w: Int, x: Int, y: Int, z: Int -> o * p * q * r * s * t * u * v * w * x * y * z }
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4)(5)(6)(7)(8)(9)(10)(11)(12), equalTo(479_001_600))
    }

    @Test
    fun testCurrying13() {
        val concat = { o: String, p: String, q: String, r: String, s: String, t: String, u: String, v: String, w: String, x: String, y: String, z: String -> o + p + q + r + s + t + u + v + w + x + y + z }
        assertThat(concat.curry()("-")("a")("bb")("ccc")("dddd")("eeeee")("f")("gg")("hhh")("iiii")("jjjjj")("k"), equalTo("-abbcccddddeeeeefgghhhiiiijjjjjk"))
    }

    @Test
    fun testCurrying14() {
        val multiply = { m: Int, n: Int, o: Int, p: Int, q: Int, r: Int, s: Int, t: Int, u: Int, v: Int, w: Int, x: Int, y: Int, z: Int -> m + n + o + p + q + r + s + t + u + v + w + x + y + z }
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4)(5)(6)(7)(8)(9)(10)(11)(12)(13)(14), equalTo(105))
    }
}
