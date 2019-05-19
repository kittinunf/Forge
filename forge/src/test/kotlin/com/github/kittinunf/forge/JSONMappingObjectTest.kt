package com.github.kittinunf.forge

import com.github.kittinunf.forge.core.Deserializable
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.apply
import com.github.kittinunf.forge.core.at
import com.github.kittinunf.forge.core.list
import com.github.kittinunf.forge.core.map
import com.github.kittinunf.forge.core.maybeAt
import com.github.kittinunf.forge.helper.deserializeDate
import com.github.kittinunf.forge.helper.toDate
import com.github.kittinunf.forge.model.Company
import com.github.kittinunf.forge.model.Friend
import com.github.kittinunf.forge.model.Friend.Address
import com.github.kittinunf.forge.model.SimpleUser
import com.github.kittinunf.forge.model.User
import com.github.kittinunf.forge.model.UserCreatedAt
import com.github.kittinunf.forge.model.UserWithCompany
import com.github.kittinunf.forge.model.UserWithFriends
import com.github.kittinunf.forge.model.UserWithOptionalFields
import com.github.kittinunf.forge.util.create
import com.github.kittinunf.forge.util.curry
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.Calendar

class JSONMappingObjectTest : BaseTest() {

    class SimpleUserDeserializer : Deserializable<SimpleUser> {
        override fun deserialize(json: JSON): DeserializedResult<SimpleUser> =
                ::SimpleUser.create
                        .map(json at "id")
                        .apply(json at "name")
    }

    @Test
    fun testSimpleUserDeserializing() {
        val result = Forge.modelFromJson(userJson, SimpleUserDeserializer())
        val (user, ex) = result

        assertThat(user!!.id, equalTo(1))
        assertThat(user.name, equalTo("Clementina DuBuque"))
        assertThat(ex, nullValue())
    }

    class UserDeserializer : Deserializable<User> {
        override fun deserialize(json: JSON): DeserializedResult<User> =
                ::User.create
                        .map(json at "id")
                        .apply(json at "username")
                        .apply(json at "name")
                        .apply(json at "age")
                        .apply(json at "email")
                        .apply(json list "levels")
                        .apply(json.at("friend", FriendDeserializer()::deserialize))
    }

    @Test
    fun testUserModelDeserializing() {
        val result = Forge.modelFromJson(userJson, UserDeserializer())
        val user: User = result.get()

        assertThat(user.id, equalTo(1))
        assertThat(user.name, equalTo("Clementina DuBuque"))
        assertThat(user.age, equalTo(46))
        assertThat(user.email, equalTo("Rey.Padberg@karina.biz"))
        assertThat(user.levels, equalTo(listOf(1, 2, 3, 4, 5)))
    }

    @Test
    fun testUserModelInUserModelDeserializing() {
        val result = Forge.modelFromJson(userJson, UserDeserializer())
        val user = result.get<User>().friend!!

        assertThat(user.id, equalTo(2))
        assertThat(user.name, equalTo("Tabitha Duran"))
        assertThat(user.address.street, equalTo("Pulaski Street"))
        assertThat(user.address.suite, equalTo("Fredericktown 8234"))
        assertThat(user.address.city, equalTo("South Carolina"))
    }

    val companyDeserializer = { json: JSON ->
        ::Company.create
                .map(json at "name")
                .apply(json at "catch_phrase")
    }

    val userModelWithCompanyDeserializer = { json: JSON ->
        ::UserWithCompany.create
                .map(json at "id")
                .apply(json at "username")
                .apply(json at "is_deleted")
                .apply(json.at("company", companyDeserializer))
    }

    @Test
    fun testUserModelWithCompanyDeserializing() {
        val result = Forge.modelFromJson(userJson, userModelWithCompanyDeserializer)
        val user: UserWithCompany = result.get()

        assertThat(user.id, equalTo(1))
        assertThat(user.username, equalTo("Moriah.Stanton"))
        assertThat(user.isDeleted, equalTo(true))
        assertThat(user.company.name, equalTo("Hoeger LLC"))
        assertThat(user.company.catchPhrase, equalTo("Centralized empowering task-force"))
    }

    class UserCreatedAt1Deserializer : Deserializable<UserCreatedAt> {

        override fun deserialize(json: JSON): DeserializedResult<UserCreatedAt> =
                ::UserCreatedAt.create
                        .map(json at "id")
                        .apply(json.at("created_at", ::deserializeDate.curry()("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")))
    }

    class UserCreatedAt2Deserializer : Deserializable<UserCreatedAt> {

        override fun deserialize(json: JSON): DeserializedResult<UserCreatedAt> =
                ::UserCreatedAt.create
                        .map(json at "id")
                        .apply(toDate() map (json at "created_at"))
    }

    @Test
    fun testUserModelCreatedAt1Deserializing() {
        val result = Forge.modelFromJson(userJson, UserCreatedAt1Deserializer())
        val (user, ex) = result

        assertThat(ex, nullValue())
        assertThat(user?.id, equalTo(1))

        val c = Calendar.getInstance()
        c.time = user?.createdAt

        assertThat(c.get(Calendar.DATE), equalTo(27))
        assertThat(c.get(Calendar.MONTH), equalTo(1))
        assertThat(c.get(Calendar.YEAR), equalTo(2015))
        assertThat(c.get(Calendar.HOUR_OF_DAY), equalTo(15))
        assertThat(c.get(Calendar.MINUTE), equalTo(10))
        assertThat(c.get(Calendar.SECOND), equalTo(39))
        assertThat(c.get(Calendar.MILLISECOND), equalTo(971))
    }

    @Test
    fun testUserModelCreatedAt2Deserializing() {
        val result = Forge.modelFromJson(userJson, UserCreatedAt2Deserializer())
        val (user, ex) = result

        assertThat(ex, nullValue())
        assertThat(user?.id, equalTo(1))

        val c = Calendar.getInstance()
        c.time = user?.createdAt

        assertThat(c.get(Calendar.DATE), equalTo(27))
        assertThat(c.get(Calendar.MONTH), equalTo(1))
        assertThat(c.get(Calendar.YEAR), equalTo(2015))
        assertThat(c.get(Calendar.HOUR), equalTo(3))
        assertThat(c.get(Calendar.AM_PM), equalTo(Calendar.PM))
        assertThat(c.get(Calendar.MINUTE), equalTo(10))
        assertThat(c.get(Calendar.SECOND), equalTo(39))
        assertThat(c.get(Calendar.MILLISECOND), equalTo(971))
    }

    class UserWithOptionalFieldsDeserializer : Deserializable<UserWithOptionalFields> {

        override fun deserialize(json: JSON): DeserializedResult<UserWithOptionalFields> =
                ::UserWithOptionalFields.create
                        .map(json at "name")
                        .apply(json maybeAt "city")
                        .apply(json maybeAt "gender")
                        .apply(json at "phone")
                        .apply(json at "weight")
    }

    @Test
    fun testUserModelWithOptionalFieldsDeserializing() {
        val result = Forge.modelFromJson(userJson, UserWithOptionalFieldsDeserializer())
        val user = result.get<UserWithOptionalFields>()

        assertThat(user.name, equalTo("Clementina DuBuque"))
        assertThat(user.city, nullValue())
        assertThat(user.gender, nullValue())
        assertThat(user.phone, equalTo("024-648-3804"))
        assertThat(user.weight, equalTo(72.5f))
    }

    class AddressDeserializer : Deserializable<Friend.Address> {

        override fun deserialize(json: JSON): DeserializedResult<Friend.Address> =
                ::Address.create
                        .map(json at "street")
                        .apply(json at "suite")
                        .apply(json at "city")
    }

    class FriendDeserializer : Deserializable<Friend> {
        override fun deserialize(json: JSON): DeserializedResult<Friend> =
                ::Friend.create
                        .map(json at "id")
                        .apply(json at "name")
                        .apply(json.at("address", AddressDeserializer()::deserialize))
    }

    class UserWithFriendsDeserializer : Deserializable<UserWithFriends> {
        override fun deserialize(json: JSON): DeserializedResult<UserWithFriends> =
                ::UserWithFriends.create
                        .map(json at "id")
                        .apply(json at "name")
                        .apply(json at "age")
                        .apply(json at "email")
                        .apply(json.list("friends", FriendDeserializer()::deserialize))
    }

    @Test
    fun testUserWithFriendsDeserializing() {
        val results = Forge.modelFromJson(userFriendsJson, UserWithFriendsDeserializer())
        val user = results.get<UserWithFriends>()

        assertThat(user, notNullValue())
        assertThat(user.friends.size, equalTo(3))

        val first = user.friends[0]
        assertThat(first.id, equalTo(10))
        assertThat(first.name, equalTo("Leanne Graham"))

        val second = user.friends[1]
        assertThat(second.name, equalTo("Ervin Howell"))
        assertThat(second.address.street, equalTo("Victor Plains"))

        val third = user.friends[2]
        assertThat(third.address.street, equalTo("Douglas Extension"))
        assertThat(third.address.suite, equalTo("Suite 847"))
    }
}
