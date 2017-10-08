import com.github.kittinunf.forge.Forge
import com.github.kittinunf.forge.core.Deserializable
import com.github.kittinunf.forge.core.DeserializedResult
import com.github.kittinunf.forge.core.JSON
import com.github.kittinunf.forge.core.apply
import com.github.kittinunf.forge.core.at
import com.github.kittinunf.forge.core.map
import com.github.kittinunf.forge.core.maybeList
import com.github.kittinunf.forge.util.create
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class JSONMappingArrayTest : BaseTest() {

    data class User(val id: Int, val username: String, val name: String, val age: Int, val email: String) {

        class Deserializer : Deserializable<User> {
            override val deserializer: (JSON) -> DeserializedResult<User> = { json ->
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

        assertThat(users.count(), equalTo(10))
        assertThat(users[0].id, equalTo(1))
        assertThat(users[1].username, equalTo("Antonette"))
        assertThat(users[2].name, equalTo("Clementine Bauch"))
        assertThat(users[3].age, equalTo(86))
        assertThat(users[4].email, equalTo("Lucio_Hettinger@annie.ca"))
    }

    @Test
    fun testUserModelWithCompanyArrayDeserializing() {
        val users = Forge.modelsFromJson(usersJson, userModelWithCompany)
        val companies = users.map { it.get<UserWithCompany>().company }

        assertThat(companies.count(), equalTo(10))
        assertThat(companies[5].name, equalTo("Considine-Lockman"))
        assertThat(companies[6].catchPhrase, equalTo("Configurable multimedia task-force"))
    }

    data class Dog(val name: String, val breed: String, val male: Boolean)

    val dogDeserializer = { j: JSON ->
        ::Dog.create.
                map(j at "name").
                apply(j at "breed").
                apply(j at "is_male")
    }

    data class UserWithDogs(val email: String, val phone: String, val dogs: List<DeserializedResult<Dog>>?)

    val userWithDogDeserializer = { j: JSON ->
        ::UserWithDogs.create.
                map(j at "email").
                apply(j at "phone").
                apply(j.maybeList("dogs", dogDeserializer))
    }

    @Test
    fun testUserWithDogsArrayDeserializing() {
        val users = Forge.modelsFromJson(usersJson, userWithDogDeserializer)
        val dogs = users.map { r: DeserializedResult<UserWithDogs> ->
            r.map { it.dogs }.let { it?.map { it.get<Dog>() } }
        }

        val firstUser = users[0].get<UserWithDogs>()
        assertThat(firstUser.email, equalTo("Sincere@april.biz"))
        assertThat(dogs[0]!!.size, equalTo(1))
        assertThat(dogs[0]!!.first().name, equalTo("Lucy"))

        val secondUser = users[1].get<UserWithDogs>()
        assertThat(secondUser.phone, equalTo("010-692-6593 x09125"))
        assertThat(dogs[1]!!.size, equalTo(2))
        assertThat(dogs[1]!!.first().breed, equalTo("Rottweiler"))
        assertThat(dogs[1]!!.last().name, equalTo("Maggie"))

        val thirdUser = users[2].get<UserWithDogs>()
        assertThat(thirdUser.email, equalTo("Nathan@yesenia.net"))
        assertThat(dogs[2], nullValue())

        val fourthUser = users[3].get<UserWithDogs>()
        assertThat(fourthUser.email, equalTo("Julianne.OConner@kory.org"))
        assertThat(dogs[3]!!.size, equalTo(1))
        assertThat(dogs[3]!!.first().name, equalTo("Cooper"))
        assertThat(dogs[3]!!.first().male, equalTo(true))

        val fifthUser = users[4].get<UserWithDogs>()
        assertThat(fifthUser.phone, equalTo("(254)954-1289"))
        assertThat(dogs[4]!!.size, equalTo(4))
        assertThat(dogs[4]!!.first().breed, equalTo("Yorkshire Terrier"))
        assertThat(dogs[4]!![1].name, equalTo("Max"))
        assertThat(dogs[4]!![2].name, equalTo("Daisy"))
        assertThat(dogs[4]!![3].male, equalTo(false))
    }

}