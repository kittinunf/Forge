import com.github.kttinunf.forge.Forge
import com.github.kttinunf.forge.core.*
import com.github.kttinunf.forge.function.toDate
import com.github.kttinunf.forge.util.create
import com.github.kttinunf.forge.util.curry
import org.json.JSONObject
import org.junit.Test
import java.util.*
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Created by Kittinun Vantasin on 8/23/15.
 */

public class JSONMappingObjectTest : BaseTest() {

    data class SimpleUser(val id: Int, val name: String) {

        class Deserializer : Deserializable<SimpleUser> {
            override val deserializer: (JSON) -> Result<SimpleUser> = { json ->
                ::SimpleUser.create.
                        map(json at "id").
                        apply(json at "name")
            }
        }

    }

    data class User(val id: Int, val name: String, val age: Int, val email: String) {

        class Deserializer : Deserializable<User> {
            override val deserializer: (JSON) -> Result<User> = { json ->
                ::User.create.
                        map(json at "id").
                        apply(json at "name").
                        apply(json at "age").
                        apply(json at "email")
            }
        }

    }

    class UserDeserializer : Deserializable<User> {

        override val deserializer: (JSON) -> Result<User> = { json ->
            ::User.create.
                    map(json at "id").
                    apply(json at "name").
                    apply(json at "age").
                    apply(json at "email")
        }

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

    data class UserCreatedAt(val id: Int, val createdAt: Date) {

        class Deserializer : Deserializable<UserCreatedAt> {

            override val deserializer: (JSON) -> Result<UserCreatedAt> = { json ->
                ::UserCreatedAt.create.
                        map(json at "id").
                        apply(toDate() map (json at "created_at"))
            }

        }

    }

    data class UserWithOptionalFields(val name: String, val city: String?, val gender: String?, val phone: String, val weight: Float) {

        class Deserializer : Deserializable<UserWithOptionalFields> {

            override val deserializer: (JSON) -> Result<UserWithOptionalFields> = { json ->
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
    fun testCurrying() {
        val multiply = { x: Int, y: Int -> x * y }
        val curry = multiply.curry()
        assertTrue { curry(2)(3) == 6 }


        val concatOfThree = { x: String, y: String, z: String -> x + y + z }
        assertTrue { concatOfThree.curry()("a")("bb")("ccc") == "abbccc" }
    }

    @Test
    fun testUserModelCurry() {
        val json = JSON.parse((JSONObject(userJson)))

        val curry = ::User.curry()

        val id: Result<Int> = (json at "id")
        val name: Result<String> = (json at "name")
        val age: Result<Int> = (json at "age")
        val email: Result<String> = (json at "email")

        val user = curry(id.get())(name.get())(age.get())(email.get())

        assertTrue { user.id == 1 }
        assertTrue { user.name == "Clementina DuBuque" }
        assertTrue { user.age == 46 }
        assertTrue { user.email == "Rey.Padberg@karina.biz" }
    }

    @Test
    fun testUserModelOptionalCurry() {
        val json = JSON.parse((JSONObject(userJson)))

        val curry = ::UserWithOptionalFields.curry()

        val name: Result<String> = (json at "name")
        val phone: Result<String> = (json at "phone")
        val weight: Result<Float> = (json at "weight")
        val city: Result<String> = (json maybeAt "city")
        val gender: Result<String> = (json maybeAt "gender")

        val user = curry(name.get())(city.get())(gender.get())(phone.get())(weight.get())

        assertTrue { user.name == "Clementina DuBuque" }
        assertTrue { user.city == null }
        assertTrue { user.gender == null }
        assertTrue { user.phone == "024-648-3804" }
        assertTrue { user.weight == 72.5f }
    }

    @Test
    fun testSimpleUserDeserializing() {
        val result = Forge.modelFromJson(userJson, SimpleUser.Deserializer())
        val (user, _) = result

        assertTrue { user!!.id == 1 }
        assertTrue { user!!.name == "Clementina DuBuque" }
    }

    @Test
    fun testUserModelInModelDeserializing() {
        val result = Forge.modelFromJson(userJson, User.Deserializer())
        val user: User = result.get()

        assertTrue { user.id == 1 }
        assertTrue { user.name == "Clementina DuBuque" }
        assertTrue { user.age == 46 }
        assertTrue { user.email == "Rey.Padberg@karina.biz" }
    }

    @Test
    fun testUserModelDeserializing() {
        val result = Forge.modelFromJson(userJson, UserDeserializer())
        val user: User = result.get()

        assertTrue { user.id == 1 }
        assertTrue { user.name == "Clementina DuBuque" }
        assertTrue { user.age == 46 }
        assertTrue { user.email == "Rey.Padberg@karina.biz" }
    }

    @Test
    fun testUserModelWithCompanyDeserializing() {
        val result = Forge.modelFromJson(userJson, userModelWithCompanyDeserializer)
        val user: UserWithCompany = result.get()

        assertTrue { user.username == "Moriah.Stanton" }
        assertTrue { user.isDeleted == true }
        assertTrue { user.company.name == "Hoeger LLC" }
        assertTrue { user.company.catchPhrase == "Centralized empowering task-force" }
    }

    @Test
    fun testUserModelCreatedAtDeserializing() {
        val result = Forge.modelFromJson(userJson, UserCreatedAt.Deserializer())
        val user: UserCreatedAt = result.get()

        assertTrue { user.id == 1 }

        val c = Calendar.getInstance()
        c.time = user.createdAt

        assertTrue { c.get(Calendar.DATE) == 27 }
        assertTrue { c.get(Calendar.MONTH) == 1 }
        assertTrue { c.get(Calendar.YEAR) == 2015 }
    }

    @Test
    fun testUserModelWithOptionalFieldsDeserializing() {
        val result = Forge.modelFromJson(userJson, UserWithOptionalFields.Deserializer())
        val user: UserWithOptionalFields = result.get()

        assertTrue { user.name == "Clementina DuBuque" }
        assertNull(user.city)
        assertNull(user.gender)
        assertTrue { user.phone == "024-648-3804" }
        assertTrue { user.weight == 72.5f }
    }

}


