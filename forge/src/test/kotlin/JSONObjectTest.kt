import com.github.kttinunf.forge.core.JSON
import com.github.kttinunf.forge.core.PropertyNotFoundException
import com.github.kttinunf.forge.core.Result
import com.github.kttinunf.forge.core.TypeMisMatchException
import org.json.JSONObject
import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Created by Kittinun Vantasin on 8/21/15.
 */

public class JSONObjectTest : BaseTest() {

    @Test
    fun testParseJSON1() {
        val j = JSONObject("{ \"hello\" : \"world\" }")
        val json = JSON.parse(j)
        assertNotNull(json.value)
    }

    @Test
    fun testParseJSON2() {
        val j = JSONObject(userJson)
        val json = JSON.parse(j)
        assertNotNull(json.value)
    }

    @Test
    fun testJSONValidValue() {
        val json = JSON.parse((JSONObject(userJson)))

        val id: Result<Int, Exception>? = json.find("id")?.valueAs()
        assertNotNull(id)
        assertTrue { id!!.get<Int>() == 1 }

        val name: Result<String, Exception>? = json.find("name")?.valueAs()

        assertNotNull(name)
        assertTrue { name!!.get<String>() == "Clementina DuBuque" }

        val isDeleted: Result<Boolean, Exception>? = json.find("is_deleted")?.valueAs()

        assertNotNull(isDeleted)
        assertTrue { isDeleted!!.get<Boolean>() == true }

        val addressStreet: Result<String, Exception>? = json.find("address.street")?.valueAs()

        assertNotNull(addressStreet)
        assertTrue { addressStreet!!.get<String>() == "Kattie Turnpike" }


        val addressGeoLat: Result<Double, Exception>? = json.find("address.geo.lat")?.valueAs()

        assertNotNull(addressGeoLat)
        assertTrue { addressGeoLat!!.get<Double>() == -38.2386 }
    }

    @Test
    fun testJSONInvalidValue() {
        val json = JSON.parse((JSONObject(userJson)))

        val notFoundName: Result<String, Exception>? = json.find("n")?.
                let { it.valueAs<String>() } ?:
                Result.Failure(PropertyNotFoundException("n"))

        assertNotNull(notFoundName!!.get())
        assertTrue { notFoundName.get<Exception>() is PropertyNotFoundException }

        val notFoundAddressSt: Result<String, Exception>? = json.find("address.st")?.
                let { it.valueAs<String>() } ?:
                Result.Failure(PropertyNotFoundException("address.st"))

        assertNotNull(notFoundAddressSt!!.get())
        assertTrue { notFoundAddressSt.get<Exception>() is PropertyNotFoundException }

        val wrongTypeEmail: Result<Float, Exception>? = json.find("email")?.
                let { it.valueAs<Float>() }

        assertTrue { wrongTypeEmail!!.get<Exception>() is TypeMisMatchException }
    }

}