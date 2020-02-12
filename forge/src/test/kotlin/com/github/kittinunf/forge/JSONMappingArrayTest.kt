package com.github.kittinunf.forge

import com.github.kittinunf.forge.JSONMappingObjectTest.FriendDeserializer
import com.github.kittinunf.forge.core.Deserializable
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.apply
import com.github.kittinunf.forge.core.at
import com.github.kittinunf.forge.core.map
import com.github.kittinunf.forge.core.maybeAt
import com.github.kittinunf.forge.core.maybeList
import com.github.kittinunf.forge.extension.lift
import com.github.kittinunf.forge.model.Company
import com.github.kittinunf.forge.model.Dog
import com.github.kittinunf.forge.model.User
import com.github.kittinunf.forge.model.UserWithCompany
import com.github.kittinunf.forge.model.UserWithDogs
import com.github.kittinunf.forge.util.create
import com.github.kittinunf.result.map
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test

class JSONMappingArrayTest : BaseTest() {

    class UserDeserializer : Deserializable<User> {
        override fun deserialize(json: JSON): DeserializedResult<User> {
            return ::User.create
                    .map(json at "id")
                    .apply(json at "username")
                    .apply(json at "name")
                    .apply(json at "age")
                    .apply(json at "email")
                    .apply(json maybeList "levels")
                    .apply(json.maybeAt("friend", FriendDeserializer()::deserialize))
        }
    }

    val companyDeserializer = { json: JSON ->
        ::Company.create
                .map(json at "name")
                .apply(json at "catch_phrase")
    }

    val userModelWithCompany = { json: JSON ->
        ::UserWithCompany.create
                .map(json at "id")
                .apply(json at "username")
                .apply(json maybeAt "is_deleted")
                .apply(json.at("company", companyDeserializer))
    }

    @Test
    fun testUserModelArrayDeserializing() {
        val results = Forge.modelsFromJson(usersJson, UserDeserializer()).lift()
        val users: List<User> = results.get()

        assertThat(users.count(), equalTo(10))
        assertThat(users[0].id, equalTo(1))
        assertThat(users[1].username, equalTo("Antonette"))
        assertThat(users[2].name, equalTo("Clementine Bauch"))
        assertThat(users[3].age, equalTo(86))
        assertThat(users[4].email, equalTo("Lucio_Hettinger@annie.ca"))
    }

    @Test
    fun testUserModelWithCompanyArrayDeserializing() {
        val users = Forge.modelsFromJson(usersJson, userModelWithCompany).lift()
        val companies = users.get().map { it.company }

        assertThat(companies.count(), equalTo(10))
        assertThat(companies[5].name, equalTo("Considine-Lockman"))
        assertThat(companies[6].catchPhrase, equalTo("Configurable multimedia task-force"))
    }

    val dogDeserializer = { j: JSON ->
        ::Dog.create
                .map(j at "name")
                .apply(j at "breed")
                .apply(j at "is_male")
    }

    val userWithDogDeserializer = { j: JSON ->
        ::UserWithDogs.create
                .map(j at "email")
                .apply(j at "phone")
                .apply(j.maybeList("dogs", dogDeserializer))
    }

    @Test
    fun testUserWithDogsArrayDeserializing() {
        val users = Forge.modelsFromJson(usersJson, userWithDogDeserializer)

        val dogs = users.map { it.map(UserWithDogs::dogs).get() }

        val firstUser = users[0].get()
        assertThat(firstUser.email, equalTo("Sincere@april.biz"))
        assertThat(dogs[0]!!.size, equalTo(1))
        assertThat(dogs[0]!!.first().name, equalTo("Lucy"))

        val secondUser = users[1].get()
        assertThat(secondUser.phone, equalTo("010-692-6593 x09125"))
        assertThat(dogs[1]!!.size, equalTo(2))
        assertThat(dogs[1]!!.first().breed, equalTo("Rottweiler"))
        assertThat(dogs[1]!!.last().name, equalTo("Maggie"))

        val thirdUser = users[2].get()
        assertThat(thirdUser.email, equalTo("Nathan@yesenia.net"))
        assertThat(dogs[2], nullValue())

        val fourthUser = users[3].get()
        assertThat(fourthUser.email, equalTo("Julianne.OConner@kory.org"))
        assertThat(dogs[3]!!.size, equalTo(1))
        assertThat(dogs[3]!!.first().name, equalTo("Cooper"))
        assertThat(dogs[3]!!.first().male, equalTo(true))

        val fifthUser = users[4].get()
        assertThat(fifthUser.phone, equalTo("(254)954-1289"))
        assertThat(dogs[4]!!.size, equalTo(4))
        assertThat(dogs[4]!!.first().breed, equalTo("Yorkshire Terrier"))
        assertThat(dogs[4]!![1].name, equalTo("Max"))
        assertThat(dogs[4]!![2].name, equalTo("Daisy"))
        assertThat(dogs[4]!![3].male, equalTo(false))
    }
}
