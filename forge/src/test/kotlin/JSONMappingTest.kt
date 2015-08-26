import BaseTest
import com.github.kttinunf.forge.Forge
import com.github.kttinunf.forge.core.Deserializable
import com.github.kttinunf.forge.core.JSON
import com.github.kttinunf.forge.core.at
import com.github.kttinunf.forge.core.map
import com.github.kttinunf.forge.function.toDate
import com.github.kttinunf.forge.util.create
import com.github.kttinunf.forge.util.curry
import org.json.JSONObject
import org.junit.Test
import java.util.Calendar
import java.util.Date
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Created by Kittinun Vantasin on 8/23/15.
 */

public class JSONMappingTest : BaseTest() {

    data class UserModel(val id: Int, val name: String, val age: Int, val email: String) {

        public class Deserializer : Deserializable<UserModel> {
            override val deserializer: (JSON) -> UserModel? = { json ->
                ::UserModel.create.
                        map(json at "id").
                        map(json at "name").
                        map(json at "age").
                        map(json at "email")
            }
        }

    }

    class UserModelDeserializer : Deserializable<UserModel> {

        override val deserializer: (JSON) -> UserModel? = { json ->
            ::UserModel.create.
                    map(json at "id").
                    map(json at "name").
                    map(json at "age").
                    map(json at "email")
        }

    }

    data class Company(val name: String, val catchPhrase: String)

    val companyDeserializer = { json: JSON ->
        ::Company.create.
                map(json at "name").
                map(json at "catch_phrase")
    }

    data class UserModelWithCompany(val username: String, val isDeleted: Boolean, val company: Company)

    val userModelWithCompanyDeserializer = { json: JSON ->
        ::UserModelWithCompany.create.
                map(json at "username").
                map(json at "is_deleted").
                map(json.at("company", companyDeserializer))
    }

    data class UserModelCreatedAt(val id: Int, val createdAt: Date) {

        class Deserializer : Deserializable<UserModelCreatedAt> {

            override val deserializer: (JSON) -> UserModelCreatedAt? = { json ->
                ::UserModelCreatedAt.create.
                        map(json at "id").
                        map(toDate() map (json at "created_at"))
            }

        }

    }

    data class UserModelWithOptionalFields(val name: String, val city: String?, val gender: String?, val phone: String, val weight: Float) {
        public class Deserializer : Deserializable<UserModelWithOptionalFields> {

            override val deserializer: (JSON) -> UserModelWithOptionalFields? = { json ->
                ::UserModelWithOptionalFields.create.
                        map(json at "name").
                        map(json at "city").
                        map(json at "gender").
                        map(json at "phone").
                        map(json at "weight")
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

            val id: Int = (json at "id")!!
            val name: String = (json at "name")!!
            val age: Int = (json at "age")!!
            val email: String = (json at "email")!!

            val user = curry(id)(name)(age)(email)

            assertTrue { user.id == 1 }
            assertTrue { user.name == "Clementina DuBuque" }
            assertTrue { user.age == 46 }
            assertTrue { user.email == "Rey.Padberg@karina.biz" }
        }

        Test
        fun testUserModelInModelDeserializing() {
            val user = Forge.fromJson(userJson, UserModel.Deserializer())

            assertTrue { user?.id == 1 }
            assertTrue { user?.name == "Clementina DuBuque" }
            assertTrue { user?.age == 46 }
            assertTrue { user?.email == "Rey.Padberg@karina.biz" }
        }

        Test
        fun testUserModelDeserializing() {
            val user = Forge.fromJson(userJson, UserModelDeserializer())

            assertTrue { user?.id == 1 }
            assertTrue { user?.name == "Clementina DuBuque" }
            assertTrue { user?.age == 46 }
            assertTrue { user?.email == "Rey.Padberg@karina.biz" }
        }

        Test
        fun testUserModelOptionalCurry() {
            val json = JSON.parse((JSONObject(userJson)))

            val curry = ::UserModelWithOptionalFields.curry()

            val name: String = (json at "name")!!
            val phone: String = (json at "phone")!!
            val weight: Float = (json at "weight")!!

            val user = curry(name)(json at "city")(json at "gender")(phone)(weight)

            assertTrue { user.name == "Clementina DuBuque" }
            assertTrue { user.city == null }
            assertTrue { user.gender == null }
            assertTrue { user.phone == "024-648-3804" }
            assertTrue { user.weight == 72.5f }
        }

        Test
        fun testUserModelWithCompanyDeserializing() {
            val user = Forge.fromJson(userJson, userModelWithCompanyDeserializer)!!

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