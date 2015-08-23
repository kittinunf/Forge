import com.github.kttinunf.forge.core.JSON
import com.github.kttinunf.forge.extension.get
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.properties.Delegates
import kotlin.test.assertNotNull

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public class JSONObjectTest {

    var userJson: String = ""

    Before
    fun setUp() {
        val dir = System.getProperty("user.dir")
        val assetsDir = File(dir, "forge/src/test/assets/")
        userJson = File(assetsDir, "user.json").readText()
    }

    Test
    fun testJSONObjectGetExtension() {
        val json = JSONObject(userJson)

        val id = json.get<Int>("id")
        val name = json.get<String>("name")
        val isDeleted = json.get<Boolean>("is_deleted")
        val addressGeoLat = json.get<Double>("address.geo.lat")
        val addressGeoLng = json.get<Double>("address.geo.lng")


        assertNotNull(id)
        assertNotNull(name)
        assertNotNull(isDeleted)
        assertNotNull(addressGeoLat)
        assertNotNull(addressGeoLng)
    }

    Test
    fun testParseJSON() {
        val json = JSON.parse(JSONObject(userJson))

        assertNotNull(json.value)
    }

}