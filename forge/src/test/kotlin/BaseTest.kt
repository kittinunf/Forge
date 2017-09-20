import org.junit.Before
import java.io.File

open class BaseTest {

    val assetsDir: File

    lateinit var userJson: String
    lateinit var userFriendsJson: String
    lateinit var usersJson: String

    init {
        val dir = System.getProperty("user.dir")
        assetsDir = File(dir, "src/test/assets/")
    }

    @Before
    fun setUp() {
        userJson = File(assetsDir, "user.json").readText()
        userFriendsJson = File(assetsDir, "user_and_friends.json").readText()
        usersJson = File(assetsDir, "users.json").readText()
    }

}
 
