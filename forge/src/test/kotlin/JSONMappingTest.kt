import BaseTest
import com.github.kttinunf.forge.core.*
import com.github.kttinunf.forge.function.toDate
import com.github.kttinunf.forge.util.curry
import org.json.JSONObject
import org.junit.Test
import java.util.*
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Created by Kittinun Vantasin on 8/23/15.
 */

public class JSONMappingTest : BaseTest() {

    data class UserModel(val id: Int, val name: String, val age: Int, val email: String) {

        class Deserializer : Deserializable<UserModel> {
            override fun deserialize(json: JSON): UserModel? {
                return ::UserModel.curry().
                        map(json at "id").
                        apply(json at "name").
                        apply(json at "age").
                        apply(json at "email")
            }

        }

    }

    data class Company(val name: String, val catchPhrase: String) {

        class Deserializer : Deserializable<Company> {
            override fun deserialize(json: JSON): Company? {
                return ::Company.curry().
                        map(json at "name").
                        apply(json at "catch_phrase")
            }
        }
    }

    data class UserModelWithCompany(val username: String, val isDeleted: Boolean, val company: Company) {

        class Deserializer : Deserializable<UserModelWithCompany> {

            override fun deserialize(json: JSON): UserModelWithCompany? {
                return ::UserModelWithCompany.curry().
                        map(json at "username").
                        apply(json at "is_deleted").
                        apply(json.at("company", Company.Deserializer()))

            }

        }

    }

    data class UserModelCreatedAt(val id: Int, val createdAt: Date) {

        class Deserializer : Deserializable<UserModelCreatedAt> {

            override fun deserialize(json: JSON): UserModelCreatedAt? {
                return ::UserModelCreatedAt.curry().
                        map(json at "id").
                        apply(toDate() map (json at "created_at"))
            }

        }

    }

    data class UserModelWithOptionalFields(val name: String, val city: String?, val gender: String?, val phone: String, val weight: Float) {

        class Deserializer : Deserializable<UserModelWithOptionalFields> {

            override fun deserialize(json: JSON): UserModelWithOptionalFields? {
                val user = ::UserModelWithOptionalFields.curry().
                        map(json at "name").
                        apply(json maybe "city").
                        apply(json maybe "gender").
                        apply(json at "phone").
                        apply(json at "weight")
                return user
            }

        }

    }


    Test
    fun testCurrying() {
        val multiply = { x: Int, y: Int -> x * y }
        val curry = multiply.curry()
        assertTrue { curry(2)(3) == 6 }


        val concatOfThree = { x: String, y: String, z: String -> x + y + z }
        assertTrue { concatOfThree.curry()("a")("bb")("ccc") == "abbccc" }
    }

    Test
    fun testUserModelCurry() {
        val json = JSON.parse((JSONObject(userJson)))

        val curry = ::UserModel.curry()

        val user = curry(json at "id")(json at "name")(json at "age")(json at "email")

        assertTrue { user.id == 1 }
        assertTrue { user.name == "Clementina DuBuque" }
        assertTrue { user.age == 46 }
        assertTrue { user.email == "Rey.Padberg@karina.biz" }
    }

    Test
    fun testUserModelOptionalCurry() {
        val json = JSON.parse((JSONObject(userJson)))

        val curry = ::UserModelWithOptionalFields.curry()

        val user = curry(json at "name")(json maybe "city")(json maybe "gender")(json at "phone")(json at "weight")

        assertTrue { user.name == "Clementina DuBuque" }
        assertTrue { user.city == null }
        assertTrue { user.gender == null }
        assertTrue { user.phone == "024-648-3804" }
        assertTrue { user.weight == 72.5f }
    }

    Test
    fun testUserModelDeserializing() {
        val user = Forge.fromJson(userJson, UserModel.Deserializer())

        assertTrue { user?.id == 1 }
        assertTrue { user?.name == "Clementina DuBuque" }
        assertTrue { user?.age == 46 }
        assertTrue { user?.email == "Rey.Padberg@karina.biz" }
    }

    Test
    fun testUserModelWithCompanyDeserializing() {
        val user = Forge.fromJson(userJson, UserModelWithCompany.Deserializer())!!

        assertTrue { user.username == "Moriah.Stanton" }
        assertTrue { user.isDeleted == true }
        assertTrue { user.company.name == "Hoeger LLC" }
        assertTrue { user.company.catchPhrase == "Centralized empowering task-force" }
    }

    Test
    fun testUserModelCreatedAtDeserializing() {
        val user = Forge.fromJson(userJson, UserModelCreatedAt.Deserializer())!!

        assertTrue { user.id == 1 }

        val c = Calendar.getInstance()
        c.setTime(user.createdAt)

        assertTrue { c.get(Calendar.DATE) == 27 }
        assertTrue { c.get(Calendar.MONTH) == 1 }
        assertTrue { c.get(Calendar.YEAR) == 2015 }
    }

    Test
    fun testUserModelWithOptionalFieldsDeserializing() {
        val user = Forge.fromJson(userJson, UserModelWithOptionalFields.Deserializer())!!

        assertTrue { user.name == "Clementina DuBuque" }
        assertNull(user.city)
        assertNull(user.gender)
        assertTrue { user.phone == "024-648-3804" }
        assertTrue { user.weight == 72.5f }
    }
}