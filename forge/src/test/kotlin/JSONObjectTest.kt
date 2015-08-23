import com.github.kttinunf.forge.core.JSON
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import java.io.File
import kotlin.properties.Delegates
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public class JSONObjectTest : BaseTest() {

    Test
    fun testParseJSON() {
        val json = JSON.parse(JSONObject(userJson))
        assertNotNull(json.value)
    }

    Test
    fun testJSONValidValue() {
        val json = JSON.parse((JSONObject(userJson)))

        val id: Int? = json.find("id")?.valueAs()
        assertNotNull(id)
        assertTrue { id == 1 }

        val name: String? = json.find("name")?.valueAs()

        assertNotNull(name)
        assertTrue { name == "Clementina DuBuque" }

        val isDeleted: Boolean? = json.find("is_deleted")?.valueAs()

        assertNotNull(isDeleted)
        assertTrue { isDeleted == true }

        val addressStreet: String? = json.find("address.street")?.valueAs()

        assertNotNull(addressStreet)
        assertTrue { addressStreet == "Kattie Turnpike" }


        val addressGeoLat: Double? = json.find("address.geo.lat")?.valueAs()

        assertNotNull(addressGeoLat)
        assertTrue { addressGeoLat == -38.2386 }
    }

    Test
    fun testJSONInvalidValue() {
        val json = JSON.parse((JSONObject(userJson)))

        val wrongName: String? = json.find("n")?.valueAs()
        assertNull(wrongName)


        val wrongAddressStreet: String? = json.find("address.st")?.valueAs()
        assertNull(wrongAddressStreet)
    }

}