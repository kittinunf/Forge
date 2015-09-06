import BaseTest
import com.github.kttinunf.forge.Forge
import com.github.kttinunf.forge.core.*
import com.github.kttinunf.forge.util.create
import org.junit.Test
import kotlin.test.assertTrue

/**
 * Created by Kittinun Vantasin on 8/27/15.
 */

public class JSONMappingArrayTest : BaseTest() {

    data class UserModel(val id: Int, val username: String, val name: String, val age: Int, val email: String) {

        class Deserializer : Deserializable<UserModel> {
            override val deserializer: (JSON) -> UserModel? = { json ->
                ::UserModel.create.
                        map(json at "id").
                        map(json at "username").
                        map(json at "name").
                        map(json at "age").
                        map(json at "email")
            }
        }

    }

    data class Company(val name: String, val catchPhrase: String)

    val companyDeserializer = { json: JSON ->
        ::Company.create.
                map(json at "name").
                map(json at "catch_phrase")
    }

    data class UserModelWithCompany(val id: Int, val username: String, val company: Company)

    val userModelWithCompany = { json: JSON ->
        ::UserModelWithCompany.create.
                map(json at "id").
                map(json at "username").
                map(json.at("company", companyDeserializer))
    }

    Test
    fun testUserModelArrayDeserializing() {
        val users = Forge.modelsFromJson(usersJson, UserModel.Deserializer())

        assertTrue { users.count() == 10 }
    }

    Test
    fun testUserModelWithCompanyArrayDeserializing() {
        val users = Forge.modelsFromJson(usersJson, userModelWithCompany)

        assertTrue {
            val companies = users.map { it?.company }
            companies.count() == 10
        }
    }

}