package com.github.kittinunf.forge.model

import java.util.Date

data class User(
    val id: Int,
    val username: String,
    val name: String,
    val age: Int,
    val email: String,
    val levels: List<Int>?,
    val friend: Friend?
)

data class Company(val name: String, val catchPhrase: String)

data class UserWithCompany(val id: Int, val username: String, val isDeleted: Boolean?, val company: Company)

data class Dog(val name: String, val breed: String, val male: Boolean)

data class UserWithDogs(val email: String, val phone: String, val dogs: List<Dog>?)

data class SimpleUser(val id: Int, val name: String)

data class UserWithOptionalFields(val name: String, val city: String?, val gender: String?, val phone: String, val weight: Float)

data class UserCreatedAt(val id: Int, val createdAt: Date)

data class Friend(val id: Int, val name: String, val address: Friend.Address) {

    data class Address(val street: String, val suite: String, val city: String)
}

data class UserWithFriends(val id: Int, val name: String, val age: Int, val email: String, val friends: List<Friend>)
