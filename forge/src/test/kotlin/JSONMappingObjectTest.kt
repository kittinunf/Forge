import com.github.kittinunf.forge.Forge
import com.github.kittinunf.forge.core.Deserializable
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.apply
import com.github.kittinunf.forge.core.at
import com.github.kittinunf.forge.core.list
import com.github.kittinunf.forge.core.map
import com.github.kittinunf.forge.core.maybeAt
import com.github.kittinunf.forge.function.toDate
import com.github.kittinunf.forge.util.create
import com.github.kittinunf.forge.util.curry
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.json.JSONObject
import org.junit.Test
import java.util.*

class JSONMappingObjectTest : BaseTest() {

    @Test
    fun testCurrying() {
        val multiply = { x: Int, y: Int -> x * y }
        val curry = multiply.curry()
        assertThat(curry(2)(3), equalTo(6))

        val concatOfThree = { x: String, y: String, z: String -> x + y + z }
        assertThat(concatOfThree.curry()("a")("bb")("ccc"), equalTo("abbccc"))
    }

    data class SimpleUser(val id: Int, val name: String) {

        class Deserializer : Deserializable<SimpleUser> {
            override val deserializer: (JSON) -> DeserializedResult<SimpleUser> = { json ->
                ::SimpleUser.create.
                        map(json at "id").
                        apply(json at "name")
            }
        }

    }

    @Test
    fun testSimpleUserDeserializing() {
        val result = Forge.modelFromJson(userJson, SimpleUser.Deserializer())
        val (user, ex) = result

        assertThat(user!!.id, equalTo(1))
        assertThat(user.name, equalTo("Clementina DuBuque"))
    }

    data class User(val id: Int, val name: String, val age: Int, val email: String) {

        class Deserializer : Deserializable<User> {
            override val deserializer: (JSON) -> DeserializedResult<User> = { json ->
                ::User.create.
                        map(json at "id").
                        apply(json at "name").
                        apply(json at "age").
                        apply(json at "email")
            }

        }

    }

    @Test
    fun testUserModelCurry() {
        val json = JSON.parse((JSONObject(userJson)))

        val curry = ::User.curry()

        val id: DeserializedResult<Int> = (json at "id")
        val name: DeserializedResult<String> = (json at "name")
        val age: DeserializedResult<Int> = (json at "age")
        val email: DeserializedResult<String> = (json at "email")

        val user = curry(id.get())(name.get())(age.get())(email.get())

        assertThat(user.id, equalTo(1))
        assertThat(user.name, equalTo("Clementina DuBuque"))
        assertThat(user.age, equalTo(46))
        assertThat(user.email, equalTo("Rey.Padberg@karina.biz"))
    }

    data class UserWithOptionalFields(val name: String, val city: String?, val gender: String?, val phone: String, val weight: Float) {

        class Deserializer : Deserializable<UserWithOptionalFields> {

            override val deserializer: (JSON) -> DeserializedResult<UserWithOptionalFields> = { json ->
                ::UserWithOptionalFields.create.
                        map(json at "name").
                        apply(json maybeAt "city").
                        apply(json maybeAt "gender").
                        apply(json at "phone").
                        apply(json at "weight")
            }

        }

    }

    @Test
    fun testUserModelOptionalCurry() {
        val json = JSON.parse((JSONObject(userJson)))

        val curry = ::UserWithOptionalFields.curry()

        val name: DeserializedResult<String> = (json at "name")
        val phone: DeserializedResult<String> = (json at "phone")
        val weight: DeserializedResult<Float> = (json at "weight")
        val city: DeserializedResult<String> = (json maybeAt "city")
        val gender: DeserializedResult<String> = (json maybeAt "gender")

        val user = curry(name.get())(city.get())(gender.get())(phone.get())(weight.get())

        assertThat(user.name, equalTo("Clementina DuBuque"))
        assertThat(user.city, nullValue())
        assertThat(user.gender, nullValue())
        assertThat(user.phone, equalTo("024-648-3804"))
        assertThat(user.weight, equalTo(72.5f))
    }

    @Test
    fun testUserModelInModelDeserializing() {
        val result = Forge.modelFromJson(userJson, User.Deserializer())
        val user: User = result.get()

        assertThat(user.id, equalTo(1))
        assertThat(user.name, equalTo("Clementina DuBuque"))
        assertThat(user.age, equalTo(46))
        assertThat(user.email, equalTo("Rey.Padberg@karina.biz"))
    }

    class UserDeserializer : Deserializable<User> {

        override val deserializer: (JSON) -> DeserializedResult<User> = { json ->
            ::User.create.
                    map(json at "id").
                    apply(json at "name").
                    apply(json at "age").
                    apply(json at "email")
        }

    }

    @Test
    fun testUserModelDeserializing() {
        val result = Forge.modelFromJson(userJson, UserDeserializer())
        val user: User = result.get()

        assertThat(user.id, equalTo(1))
        assertThat(user.name, equalTo("Clementina DuBuque"))
        assertThat(user.age, equalTo(46))
        assertThat(user.email, equalTo("Rey.Padberg@karina.biz"))
    }

    data class Company(val name: String, val catchPhrase: String)

    val companyDeserializer = { json: JSON ->
        ::Company.create.
                map(json at "name").
                apply(json at "catch_phrase")
    }

    data class UserWithCompany(val username: String, val isDeleted: Boolean, val company: Company)

    val userModelWithCompanyDeserializer = { json: JSON ->
        ::UserWithCompany.create.
                map(json at "username").
                apply(json at "is_deleted").
                apply(json.at("company", companyDeserializer))
    }

    @Test
    fun testUserModelWithCompanyDeserializing() {
        val result = Forge.modelFromJson(userJson, userModelWithCompanyDeserializer)
        val user: UserWithCompany = result.get()

        assertThat(user.username, equalTo("Moriah.Stanton"))
        assertThat(user.isDeleted, equalTo(true))
        assertThat(user.company.name, equalTo("Hoeger LLC"))
        assertThat(user.company.catchPhrase, equalTo("Centralized empowering task-force"))
    }

    data class UserCreatedAt(val id: Int, val createdAt: Date) {

        class Deserializer : Deserializable<UserCreatedAt> {

            override val deserializer: (JSON) -> DeserializedResult<UserCreatedAt> = { json ->
                ::UserCreatedAt.create.
                        map(json at "id").
                        apply(toDate() map (json at "created_at"))
            }

        }

    }

    @Test
    fun testUserModelCreatedAtDeserializing() {
        val result = Forge.modelFromJson(userJson, UserCreatedAt.Deserializer())
        val user: UserCreatedAt = result.get()

        assertThat(user.id, equalTo(1))

        val c = Calendar.getInstance()
        c.time = user.createdAt

        assertThat(c.get(Calendar.DATE), equalTo(27))
        assertThat(c.get(Calendar.MONTH), equalTo(1))
        assertThat(c.get(Calendar.YEAR), equalTo(2015))
    }

    @Test
    fun testUserModelWithOptionalFieldsDeserializing() {
        val result = Forge.modelFromJson(userJson, JSONMappingObjectTest.UserWithOptionalFields.Deserializer())
        val user: JSONMappingObjectTest.UserWithOptionalFields = result.get()

        assertThat(user.name, equalTo("Clementina DuBuque"))
        assertThat(user.city, nullValue())
        assertThat(user.gender, nullValue())
        assertThat(user.phone, equalTo("024-648-3804"))
        assertThat(user.weight, equalTo(72.5f))
    }

    data class Friend(val id: Int, val name: String, val address: Friend.Address) {

        data class Address(val street: String, val suite: String, val city: String) {

            class Deserializer : Deserializable<Address> {

                override val deserializer: (JSON) -> DeserializedResult<Address> = { json ->
                    ::Address.create.
                            map(json at "street").
                            apply(json at "suite").
                            apply(json at "city")
                }

            }

        }

        class Deserializer : Deserializable<Friend> {
            override val deserializer: (JSON) -> DeserializedResult<Friend> = {
                ::Friend.create.
                        map(it at "id").
                        apply(it at "name").
                        apply(it.at("address", Friend.Address.Deserializer().deserializer))
            }
        }

    }

    data class UserWithFriends(val id: Int, val name: String, val age: Int, val email: String, val friends: List<DeserializedResult<Friend>>) {

        class Deserializer : Deserializable<UserWithFriends> {
            override val deserializer: (JSON) -> DeserializedResult<UserWithFriends> = { json ->
                ::UserWithFriends.create.
                        map(json at "id").
                        apply(json at "name").
                        apply(json at "age").
                        apply(json at "email").
                        apply(json.list("friends", Friend.Deserializer().deserializer))
            }
        }

    }

    @Test
    fun testUserWithFriendsDeserializing() {
        val results = Forge.modelFromJson(userFriendsJson, UserWithFriends.Deserializer())
        val user: UserWithFriends = results.get()

        assertThat(user, notNullValue())
        assertThat(user.friends.size, equalTo(3))

        val first = user.friends[0].get<Friend>()
        assertThat(first.id, equalTo(10))
        assertThat(first.name, equalTo("Leanne Graham"))


        val second = user.friends[1].get<Friend>()
        assertThat(second.name, equalTo("Ervin Howell"))
        assertThat(second.address.street, equalTo("Victor Plains"))

        val third = user.friends[2].get<Friend>()
        assertThat(third.address.street, equalTo("Douglas Extension"))
        assertThat(third.address.suite, equalTo("Suite 847"))
    }

}
