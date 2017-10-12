package com.github.kittinunf.forge

import org.junit.Before
import java.io.File

open class BaseTest {

    private val assetsDir: File

    val userJson: String
    val userFriendsJson: String
    val usersJson: String

    init {
        val dir = System.getProperty("user.dir")
        val file = File(dir)

        assetsDir = file.walkTopDown().filter { it.name == "assets" }.first()
        userJson = File(assetsDir, "user.json").readText()
        userFriendsJson = File(assetsDir, "user_and_friends.json").readText()
        usersJson = File(assetsDir, "users.json").readText()
    }

    @Before
    fun setUp() {}

}
 
