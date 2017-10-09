import com.github.kittinunf.forge.util.create
import com.github.kittinunf.forge.util.curry

class CurryingTest {

    @Test
    fun testCurrying2() {
        val multiply = { x: Int, y: Int -> x * y }
        val curry = multiply.create
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
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4), equalTo(24))
    }

    @Test
    fun testCurrying5() {
        val concat = { v: String, w: String, x: String, y: String, z: String -> v + w + x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc")("dddd")("eeeee"), equalTo("abbcccddddeeeee"))
    }

    @Test
    fun testCurrying6() {
        val multiply = { u: Int, v: Int, w: Int, x: Int, y: Int, z: Int -> u * v * w * x * y * z }
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4)(5)(6), equalTo(720))
    }

    @Test
    fun testCurrying7() {
        val concat = { t: String, u: String, v: String, w: String, x: String, y: String, z: String -> t + u + v + w + x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc")("dddd")("eeeee")("f")("gg"), equalTo("abbcccddddeeeeefgg"))
    }

    @Test
    fun testCurrying8() {
        val multiply = { s: Int, t: Int, u: Int, v: Int, w: Int, x: Int, y: Int, z: Int -> s * t * u * v * w * x * y * z }
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4)(5)(6)(7)(8), equalTo(40_320))
    }

    @Test
    fun testCurrying9() {
        val concat = { r: String, s: String, t: String, u: String, v: String, w: String, x: String, y: String, z: String -> r + s + t + u + v + w + x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc")("dddd")("eeeee")("f")("gg")("hhh")("iiii"), equalTo("abbcccddddeeeeefgghhhiiii"))
    }

    @Test
    fun testCurrying10() {
        val multiply = { q: Int, r: Int, s: Int, t: Int, u: Int, v: Int, w: Int, x: Int, y: Int, z: Int -> q * r * s * t * u * v * w * x * y * z }
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4)(5)(6)(7)(8)(9)(10), equalTo(3_628_800))
    }

    @Test
    fun testCurrying11() {
        val concat = { p: String, q: String, r: String, s: String, t: String, u: String, v: String, w: String, x: String, y: String, z: String -> p + q + r + s + t + u + v + w + x + y + z }
        assertThat(concat.curry()("a")("bb")("ccc")("dddd")("eeeee")("f")("gg")("hhh")("iiii")("jjjjj")("k"), equalTo("abbcccddddeeeeefgghhhiiiijjjjjk"))
    }

    @Test
    fun testCurrying12() {
        val multiply = { o: Int, p: Int, q: Int, r: Int, s: Int, t: Int, u: Int, v: Int, w: Int, x: Int, y: Int, z: Int -> o * p * q * r * s * t * u * v * w * x * y * z }
        val curry = multiply.create
        assertThat(curry(1)(2)(3)(4)(5)(6)(7)(8)(9)(10)(11)(12), equalTo(479_001_600))
    }

}