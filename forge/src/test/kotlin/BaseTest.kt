import org.junit.Before
import java.io.File
import kotlin.properties.Delegates

/**
 * Created by Kittinun Vantasin on 8/23/15.
 */

public open class BaseTest {

    val assetsDir: File

    var userJson: String by Delegates.notNull()

    init {
        val dir = System.getProperty("user.dir")
        assetsDir = File(dir, "forge/src/test/assets/")
    }

    Before
    fun setUp() {
        userJson = File(assetsDir, "user.json").readText()
    }

}
 
