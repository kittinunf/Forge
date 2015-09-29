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

    data class User(val id: Int, val username: String, val name: String, val age: Int, val email: String) {

        class Deserializer : Deserializable<User> {
            override val deserializer: (JSON) -> Result<User, Exception> = { json ->
                ::User.create.
                        map(json at "id").
                        apply(json at "username").
                        apply(json at "name").
                        apply(json at "age").
                        apply(json at "email")
            }
        }

    }

    data class Company(val name: String, val catchPhrase: String)

    val companyDeserializer = { json: JSON ->
        ::Company.create.
                map(json at "name").
                apply(json at "catch_phrase")
    }

    data class UserWithCompany(val id: Int, val username: String, val company: Company)

    val userModelWithCompany = { json: JSON ->
        ::UserWithCompany.create.
                map(json at "id").
                apply(json at "username").
                apply(json.at("company", companyDeserializer))
    }

    @Test
    fun testUserModelArrayDeserializing() {
        val results = Forge.modelsFromJson(usersJson, User.Deserializer())
        val users: List<User> = results.map { it.get<User>() }

        assertTrue { users.count() == 10 }
        assertTrue { users[0].id == 1 }
        assertTrue { users[1].username == "Antonette" }
        assertTrue { users[2].name == "Clementine Bauch" }
        assertTrue { users[3].age == 86 }
        assertTrue { users[4].email == "Lucio_Hettinger@annie.ca" }
    }

    @Test
    fun testUserModelWithCompanyArrayDeserializing() {
        val users = Forge.modelsFromJson(usersJson, userModelWithCompany)
        val companies = users.map { it.get<UserWithCompany>().company }

        assertTrue { companies.count() == 10 }
        assertTrue { companies[5].name == "Considine-Lockman" }
        assertTrue { companies[6].catchPhrase == "Configurable multimedia task-force" }
    }

}