import com.github.kttinunf.forge.Forge
import com.github.kttinunf.forge.core.*
import com.github.kttinunf.forge.util.create
import org.junit.Test
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Created by Kittinun Vantasin on 8/27/15.
 */

public class JSONMappingArrayTest : BaseTest() {

    data class User(val id: Int, val username: String, val name: String, val age: Int, val email: String) {

        class Deserializer : Deserializable<User> {
            override val deserializer: (JSON) -> Result<User> = { json ->
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

    data class Dog(val name: String, val breed: String, val male: Boolean)

    val dogDeserializer = { j: JSON ->
        ::Dog.create.
                map(j at "name").
                apply(j at "breed").
                apply(j at "is_male")
    }

    data class UserWithDogs(val email: String, val phone: String, val dogs: List<Result<Dog>>?)

    val userWithDogDeserializer = { j: JSON ->
        ::UserWithDogs.create.
                map(j at "email").
                apply(j at "phone").
                apply(j.maybeList("dogs", dogDeserializer))
    }

    @Test
    fun testUserWithDogsArrayDeserializing() {
        val users = Forge.modelsFromJson(usersJson, userWithDogDeserializer)
        val dogs = users.map { r: Result<UserWithDogs> ->
            r.map { it.dogs }.let { it?.map { it.get<Dog>() } }
        }

        assertTrue { users[0].let { it.email == "Sincere@april.biz" } ?: false }
        assertTrue { dogs[0]!!.size() == 1 }
        assertTrue { dogs[0]!!.first().name == "Lucy" }

        assertTrue { users[1].let { it.phone == "010-692-6593 x09125" } ?: false }
        assertTrue { dogs[1]!!.size() == 2 }
        assertTrue { dogs[1]!!.first().breed == "Rottweiler" }
        assertTrue { dogs[1]!!.last().name == "Maggie" }

        assertTrue { users[2].let { it.email == "Nathan@yesenia.net" } ?: false }
        assertNull(dogs[2])

        assertTrue { users[3].let { it.email == "Julianne.OConner@kory.org" } ?: false }
        assertTrue { dogs[3]!!.size() == 1 }
        assertTrue { dogs[3]!!.first().name == "Cooper" }
        assertTrue { dogs[3]!!.first().male == true }

        assertTrue { users[4].let { it.phone == "(254)954-1289" } ?: false }
        assertTrue { dogs[4]!!.size() == 4 }
        assertTrue { dogs[4]!!.first().breed == "Yorkshire Terrier" }
        assertTrue { dogs[4]!![1].name == "Max" }
        assertTrue { dogs[4]!![2].name == "Daisy" }
        assertTrue { dogs[4]!![3].male == false }

    }

}