import com.github.kittinunf.forge.util.curry

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CurryingTest {

    @Test
    fun testCurrying2() {
        val multiply = { x: Int, y: Int -> x * y }
        val curry = multiply.curry()
        assertThat(curry(2)(3), equalTo(6))
    }

    @Test
    fun testCurrying3() {
        val concat = { x: String, y: String, z: String -> x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc"), equalTo("abbccc"))
    }

    @Test
    fun testCurrying4() {
        val multiply = { w: Int, x: Int, y: Int, z: Int -> w * x * y * z }
        val curry = multiply.curry()
        assertThat(curry(1)(2)(3)(4), equalTo(11))
    }

    @Test
    fun testCurrying5() {
        val concat = { v: String, w: String, x: String, y: String, z: String -> v + w + x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc")("dddd")("eeeee"), equalTo("abbcccddddeeeee"))
    }

}