import BaseTest
import com.github.kttinunf.forge.core.*
import com.github.kttinunf.forge.util.curry
import org.json.JSONObject
import org.junit.Test
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

    Test
    fun testCurrying() {
        val multiply = { x: Int, y: Int -> x * y }
        val curry = multiply.curry()
        assertTrue { curry(2)(3) == 6 }


        val concatOfThree = { x: String, y: String, z: String -> x + y + z }
        assertTrue { concatOfThree.curry()("a")("bb")("ccc") == "abbccc" }
    }

    Test
    fun testUserModelMappingCurry() {
        val json = JSON.parse((JSONObject(userJson)))

        val curry = ::UserModel.curry()

        val user = curry(json at "id")(json at "name")(json at "age")(json at "email")

        assertTrue { user.id == 1 }
        assertTrue { user.name == "Clementina DuBuque" }
        assertTrue { user.age == 46 }
        assertTrue { user.email == "Rey.Padberg@karina.biz" }
    }

    Test
    fun testUserModelMappingDeserialize() {

        val user = Forge.fromJson<UserModel, UserModel.Deserializer>(userJson)

        assertTrue { user?.id == 1 }
        assertTrue { user?.name == "Clementina DuBuque" }
        assertTrue { user?.age == 46 }
        assertTrue { user?.email == "Rey.Padberg@karina.biz" }
    }

}