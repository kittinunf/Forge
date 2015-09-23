import BaseTest
import com.github.kttinunf.forge.Forge
import com.github.kttinunf.forge.core.*
import com.github.kttinunf.forge.util.create
import org.junit.Test
import kotlin.test.assertNotNull

/**
 * Created by Kittinun Vantasin on 9/8/15.
 */

public class JSONMappingObjectNestedArrayTest : BaseTest() {

//    data class UserWithFriends(val id: Int, val name: String, val age: Int, val email: String, val friend: List<UserWithFriends?>) {
//
//        class Deserializer : Deserializable<UserWithFriends> {
//            override val deserializer: (JSON) -> UserWithFriends? = { json ->
//                ::UserWithFriends.create.
//                        map(json at "id").
//                        map(json at "name").
//                        map(json at "age").
//                        map(json at "email").
//                        map(json.list("friends", UserWithFriends.Deserializer().deserializer))
//            }
//        }
//
//    }
//
//    Test
//    fun testUserWithFriendsDeserializing() {
//        val user = Forge.modelFromJson(userFriendsJson, UserWithFriends.Deserializer())
//
//        assertNotNull(user)
//    }
}