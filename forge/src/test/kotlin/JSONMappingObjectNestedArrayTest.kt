import com.github.kttinunf.forge.Forge
import com.github.kttinunf.forge.core.*
import com.github.kttinunf.forge.util.create
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Created by Kittinun Vantasin on 9/8/15.
 */

public class JSONMappingObjectNestedArrayTest : BaseTest() {

    data class Friend(val id: Int, val name: String, val address: Friend.Address) {

        data class Address(val street: String, val suite: String, val city: String) {

            class Deserializer : Deserializable<Address> {

                override val deserializer: (JSON) -> Result<Address> = { json ->
                    ::Address.create.
                            map(json at "street").
                            apply(json at "suite").
                            apply(json at "city")
                }

            }

        }

        class Deserializer : Deserializable<Friend> {
            override val deserializer: (JSON) -> Result<Friend> = {
                ::Friend.create.
                        map(it at "id").
                        apply(it at "name").
                        apply(it.at("address", Friend.Address.Deserializer().deserializer))
            }
        }

    }

    data class UserWithFriends(val id: Int, val name: String, val age: Int, val email: String, val friends: List<Result<Friend>>) {

        class Deserializer : Deserializable<UserWithFriends> {
            override val deserializer: (JSON) -> Result<UserWithFriends> = { json ->
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

        assertNotNull(user)
        assertTrue { user.friends.size() == 3 }

        assertTrue { user.friends[0].let { friend ->
            friend.id == 10
            friend.name == "Leanne Graham"
        } ?: false }

        assertTrue { user.friends[1].let { friend ->
            friend.name == "Ervin Howell"
            friend.address.street == "Victor Plains"
        } ?: false }

        assertTrue { user.friends[2].let { friend ->
            friend.address.street == "Douglas Extension"
            friend.address.suite == "Suite 847"
        } ?: false }
    }
}